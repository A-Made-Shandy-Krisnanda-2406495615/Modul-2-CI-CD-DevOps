# Eshop (Spring Boot) — Refleksi

Repository ini menggunakan Spring Boot (Spring MVC + Thymeleaf) dengan arsitektur sederhana **Controller -> Service -> Repository**. Pada modul ini saya menambahkan dua fitur backend: **delete product** dan **edit product** (berdasarkan `productId`). Sesuai requirement tugas, fokusnya ada di **backend**, bukan UI/HTML.

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
   - Perbaikan: gunakan database 

6) **Error handling masih minimal**  
   Banyak method mengembalikan `null` saat gagal.
   - Perbaikan: gunakan `Optional`, atau lempar exception yang jelas dan tangani dengan `@ControllerAdvice` (mis. 404 saat product tidak ditemukan).

