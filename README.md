# Eshop (Spring Boot) — Refleksi

Repository ini menggunakan Spring Boot (Spring MVC + Thymeleaf) dengan arsitektur sederhana **Controller -> Service -> Repository**. Pada modul ini saya menambahkan dua fitur backend: **delete product** dan **edit product** (berdasarkan `productId`). Sesuai requirement tugas, fokusnya ada di **backend**, bukan UI/HTML.

## Refleksi 1

## Fitur yang ditambahkan (Backend)

- **Delete Product**
  - Endpoint: `POST /product/delete/{id}`
  - Tujuan: menghapus product berdasarkan `productId`.
- **Edit Product**
  - Endpoint: `GET /product/edit/{id}` untuk mengambil data product yang akan diedit.
  - Endpoint: `POST /product/edit/{id}` untuk menyimpan perubahan product.
- **ID Produk**
  - `productId` dibuat menggunakan UUID ketika product dibuat (jika belum ada ID), agar ID unik dan tidak mudah ditebak.

## Prinsip Clean Code yang diterapkan

- **Separation of Concerns (SoC)**  
  Controller hanya mengurus request/response, Service mengurus logic, dan Repository mengurus akses data (walaupun masih in-memory).
- **Single Responsibility**  
  Tiap class punya tanggung jawab yang jelas: `ProductController`, `ProductServiceImpl`, `ProductRepository`.
- **Metode kecil dan fokus**  
  Tiap endpoint/controller method berusaha mengerjakan 1 tujuan (create/list/delete/edit).
- **Penamaan yang cukup deskriptif**  
  Nama class dan method menggambarkan domain (`Product`, `ProductService`, `ProductRepository`).

## Praktik Secure Coding yang sudah dipakai/dipertimbangkan

- **Menghindari perubahan state lewat GET**  
  Operasi yang mengubah data (delete/edit submit) menggunakan `POST`, bukan `GET`.
- **ID unik (UUID)**  
  Mengurangi risiko collision dan mempersulit enumeration dibanding ID berurutan.
- **Validasi “entity exists” pada edit (di controller)**  
  Saat `GET /edit/{id}`, controller mengecek apakah product ditemukan; jika tidak, redirect ke list.

## Temuan masalah pada source code & cara memperbaiki

1) **Potensi `NullPointerException` di `deleteById`**  
   `product.getProductId().equals(id)` akan error bila `productId` bernilai `null`.
   - Perbaikan: gunakan `Objects.equals(product.getProductId(), id)` dan/atau pastikan setiap product selalu memiliki ID.

2) **`POST /edit/{id}` belum memanfaatkan `id` path variable**  
   Di controller, method edit menerima `@PathVariable String id` tetapi mengirim `Product` ke service tanpa memastikan `productId` = `id`. Ini berisiko:
   - Update gagal karena `productId` kosong/beda.
   - Overposting: klien bisa menyisipkan field yang tidak seharusnya diubah (kalau model bertambah).
   - Perbaikan: set `product.setProductId(id)` sebelum memanggil service, dan/atau gunakan DTO khusus edit (mis. `UpdateProductRequest`).

3) **Belum ada validasi input**  
   `productName` bisa kosong dan `productQuantity` bisa negatif bila tidak divalidasi.
   - Perbaikan: tambahkan Bean Validation (`@NotBlank`, `@Min(0)`) pada model/DTO, pakai `@Valid` di controller, dan tangani `BindingResult`.

4) **Masih in-memory dan belum thread-safe**  
   Repository memakai `ArrayList` biasa, data hilang saat restart, dan akses concurrent bisa bermasalah.
   - Perbaikan: gunakan database (mis. Spring Data JPA) atau minimal gunakan struktur data thread-safe dan tambahkan mekanisme reset state untuk testing.

5) **Redirect relatif bisa salah tujuan**  
   Penggunaan `redirect:list` bersifat relatif terhadap path saat ini, sehingga dari `/product/edit/{id}` bisa menjadi `/product/edit/list`.
   - Perbaikan: gunakan redirect absolut seperti `redirect:/product/list`.

6) **Error handling masih minimal**  
   Banyak method mengembalikan `null` saat gagal.
   - Perbaikan: gunakan `Optional`, atau lempar exception yang jelas dan tangani dengan `@ControllerAdvice` (mis. 404 saat product tidak ditemukan).

## Refleksi 2

### 1) Setelah menulis unit test, apa yang saya rasakan? Berapa banyak unit test per class? Apakah code coverage 100% berarti bebas bug?

Setelah menulis unit test, saya merasa lebih “tenang” karena perubahan kecil di code bisa divalidasi cepat tanpa harus mencoba manual lewat UI. Unit test juga membantu saya memikirkan ulang desain (misalnya: apakah method terlalu besar, apakah ada dependency yang sulit di-mock, dan apakah logiknya sudah mudah diuji).

Jumlah unit test **tidak bisa ditentukan dengan angka pasti per class**. Yang lebih penting adalah cakupan perilaku dan risiko:

- Setiap **public behavior** penting sebaiknya punya test: skenario normal + edge case.
- Test perlu mencakup **branch penting** (if/else), input tidak valid, dan kondisi batas (mis. quantity = 0, name kosong, id tidak ditemukan).
- Jika sebuah class punya banyak cabang logika, biasanya test-nya juga akan lebih banyak.

Agar unit test “cukup” untuk memverifikasi program, saya bisa melakukan:

- Mapping test ke requirement/use case (create, list, edit, delete).
- Tambahkan test untuk **negative path** (mis. edit id tidak ada, delete id tidak ada).
- Gunakan **code coverage** (line/branch coverage) sebagai indikator area yang belum tersentuh test, tetapi **bukan target utama**.

Code coverage 100% **tidak menjamin** code bebas bug/error. Alasannya:

- Coverage hanya menunjukkan baris/branch dieksekusi, bukan kualitas assertion (test bisa “jalan” tapi tidak benar-benar memverifikasi output).
- Bug bisa muncul dari kombinasi input yang tidak diuji, interaksi antar komponen, concurrency, atau perilaku environment (mis. DB, network).
- Requirement bisa saja salah dipahami; test dengan coverage tinggi tetap bisa “mengunci” perilaku yang keliru.

Jadi, coverage saya anggap sebagai **alat bantu** untuk menemukan blind spot, bukan bukti final bahwa aplikasi sudah benar.

### 2) Jika membuat functional test baru untuk mengecek jumlah item pada product list dengan setup yang sama, apa dampaknya ke clean code? Apa yang bisa diperbaiki?

Kalau saya membuat functional test suite baru dengan cara “copy-paste” setup yang sama (port, baseUrl, driver navigation, locator, dsb.), maka dari sisi clean code itu cenderung **mengurangi kualitas** karena:

- **Duplikasi kode (melanggar DRY)**: setup `baseUrl`, `WebDriverWait`, dan langkah navigasi berulang di banyak class. Kalau ada perubahan kecil (mis. judul halaman berubah), saya harus mengubah banyak file -> rawan lupa.
- **Magic string & locator tersebar**: selector seperti `By.linkText("View Products")`, `By.id("nameInput")`, dan judul halaman tersebar di banyak test -> brittle dan susah dirawat.
- **Test jadi sulit dibaca**: langkah-langkah UI yang panjang bercampur dengan assertion, sehingga niat test (apa yang divalidasi) tidak langsung terlihat.
- **Potensi ketergantungan state**: karena repository masih in-memory, data bisa “terbawa” antar test dalam satu context. Functional test yang memeriksa “jumlah item” bisa flakey kalau tidak memastikan state awal.

Perbaikan yang bisa dilakukan agar lebih clean:

- Buat **base class/helper** untuk functional test (mis. `BaseFunctionalTest`) yang memusatkan pembuatan `baseUrl` dan helper method (mis. `openHome()`, `openProductList()`, `openCreateProduct()`).
- Terapkan **Page Object Pattern** (mis. `HomePage`, `ProductListPage`, `CreateProductPage`) supaya locator dan aksi UI tersentralisasi, test lebih “bercerita” dan mudah dipahami.
- Buat **konstanta** untuk teks penting (judul halaman, id element) agar perubahan UI tidak menyebar.
- Pastikan **test isolation** untuk skenario “jumlah item”: reset data sebelum test (mis. `@DirtiesContext` untuk functional test tertentu) atau siapkan mekanisme clear repository khusus testing.

Dengan refactor seperti itu, menambah functional test baru (termasuk cek jumlah item) akan lebih cepat, lebih stabil, dan lebih mudah dipelihara.
