# Eshop (Spring Boot) 
Link : https://a-made-shandy-krisnanda-modul-7a19d7cc45a6.herokuapp.com
## Reflection

### 1) Explain what principles you apply to your project
Saya menerapkan:
- SRP: `Controller` fokus HTTP flow, `Service` fokus business logic, `Repository` fokus data access.
- OCP: perilaku data layer bisa diperluas lewat `ProductRepositoryPort` dan `CarRepositoryPort` tanpa mengubah high-level module.
- LSP: `CarController` dipisah dari `ProductController` sehingga tidak ada pewarisan yang melanggar substitusi.
- ISP: interface dipisah per domain (`ProductService`, `CarService`, `ProductRepositoryPort`, `CarRepositoryPort`) agar client hanya bergantung ke method yang dipakai.
- DIP: `Controller` dan `Service` bergantung ke abstraksi (service/repository interface), bukan implementasi konkret.

### 2) Explain the advantages of applying SOLID principles with examples
- Kode lebih mudah dirawat: perubahan route/product flow tidak ikut mengganggu car flow karena controller sudah terpisah.
- Kode lebih mudah di-extend: kita bisa menambah implementasi repository lain selama mengikuti `*RepositoryPort`.
- Testing lebih mudah: dependency dapat di-mock lewat interface, contoh test `CarControllerTest` dan `ProductServiceTest`.
- Risiko efek samping berkurang: pembagian tanggung jawab membuat perubahan lebih terlokalisasi.

### 3) Explain the disadvantages of not applying SOLID principles with examples
- Coupling tinggi: jika controller langsung memakai class implementasi, perubahan kecil di low-level module bisa merambat ke banyak file.
- Sulit dikembangkan: tanpa OCP/port, setiap penambahan variasi data access cenderung memaksa modifikasi class yang sudah stabil.
- Sulit diuji: tanpa DIP/ISP, unit test jadi berat karena object bergantung pada banyak detail konkret.
- Rawan regresi: ketika tanggung jawab campur dalam satu class, satu perubahan kecil bisa merusak fitur lain yang tidak terkait.
