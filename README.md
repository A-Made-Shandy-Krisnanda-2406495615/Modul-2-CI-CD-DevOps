# Eshop (Spring Boot) 
Link : https://a-made-shandy-krisnanda-modul-7a19d7cc45a6.herokuapp.com
## Reflection

### 1) Explain what principles you apply to your project
Saya menerapkan:
- SRP (Single Responsibility Principle): tanggung jawab dipisah per layer.
  `ProductController` hanya menangani HTTP flow produk (`createProductPage`, `productListPage`, `editProductPost`, `deleteProduct`), `ProductServiceImpl` menangani business rule (mis. generate UUID pada `create`), dan `ProductRepository` fokus data access (`create`, `findAll`, `findById`, `deleteById`).
  Pola yang sama juga dipakai pada domain mobil (`CarController`, `CarServiceImpl`, `CarRepository`).
- OCP (Open/Closed Principle): high-level module tidak tergantung implementasi konkret.
  `ProductServiceImpl` bergantung ke `ProductRepositoryPort`, dan `CarServiceImpl` bergantung ke `CarRepositoryPort`.
  Artinya, jika nanti ingin tambah adapter baru (mis. database/JPA), cukup buat implementasi baru dari port tanpa mengubah logic service/controller.
- LSP (Liskov Substitution Principle): implementasi bisa disubstitusi melalui abstraksi tanpa merusak flow utama.
  Controller memakai `ProductService`/`CarService` interface, sehingga implementasi service bisa diganti selama kontraknya sama.
  Repository juga mengikuti kontrak port (`ProductRepositoryPort`, `CarRepositoryPort`) sehingga service tetap bekerja ketika implementasi repository diganti.
- ISP (Interface Segregation Principle): interface dipisah agar client tidak bergantung ke method yang tidak dipakai.
  Contoh: `ProductService` hanya berisi operasi produk, `CarService` hanya operasi mobil; `ProductRepositoryPort` dan `CarRepositoryPort` juga dipisah per domain.
- DIP (Dependency Inversion Principle): modul high-level bergantung ke abstraksi.
  `ProductController` menerima `ProductService` lewat constructor injection, `CarController` menerima `CarService`, dan service menerima repository port, bukan class konkret.

### 2) Explain the advantages of applying SOLID principles with examples
- Maintainability naik: perubahan pada flow produk tidak mengganggu flow mobil karena class dipisah.
  Contoh: ubah endpoint/logic edit produk di `ProductController` tidak perlu menyentuh `CarController`.
- Extensibility lebih baik: mudah menambah implementasi baru tanpa ubah core logic.
  Contoh: saat ingin pindah dari in-memory ke persistence lain, cukup buat implementasi baru dari `ProductRepositoryPort`/`CarRepositoryPort`.
- Testability lebih baik: dependency mudah di-mock karena memakai interface.
  Contoh: `CarControllerTest` mock `CarService`; `ProductServiceTest` mock `ProductRepositoryPort`; test fokus pada perilaku unit masing-masing.
- Risiko regresi berkurang: perubahan jadi terlokalisasi di layer terkait.
  Contoh: perubahan struktur penyimpanan di `CarRepository` tidak memaksa perubahan di `CarController`.

### 3) Explain the disadvantages of not applying SOLID principles with examples
- Coupling tinggi: jika controller langsung membuat instance repository konkret, perubahan low-level mudah merambat ke layer atas.
  Contoh: ganti cara simpan data akan memaksa edit controller dan service sekaligus.
- Sulit dikembangkan: tanpa OCP, penambahan fitur/persistence baru cenderung mengubah class yang sudah stabil.
  Contoh: menambah backend penyimpanan baru harus mengedit logic lama alih-alih menambah class implementasi baru.
- Sulit diuji: tanpa DIP/ISP, unit test harus menyiapkan banyak dependency konkret.
  Contoh: test controller jadi tidak bisa fokus ke validasi request/response karena bercampur dengan detail data access.
- Rawan bug lintas fitur: tanpa SRP, satu class mengurus banyak hal dan satu perubahan dapat memengaruhi fitur lain.
  Contoh: perubahan di proses update mobil bisa tidak sengaja merusak list atau delete jika semua logic menumpuk pada class yang sama.
