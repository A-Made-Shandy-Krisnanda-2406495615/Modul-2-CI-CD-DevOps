# Eshop (Spring Boot) 
Link : https://a-made-shandy-krisnanda-modul-7a19d7cc45a6.herokuapp.com
## Refleksi 4.2

### 1) Daftar code quality issue yang diperbaiki dan strategi perbaikannya

Selama latihan ini, saya memperbaiki dua isu utama dari hasil analisis statis kode.

Pertama, pada `ProductController` terdapat duplikasi string literal `"redirect:/product/list"` sebanyak beberapa kali (rule `java:S1192`). Strategi perbaikannya adalah mengekstrak literal tersebut menjadi konstanta `REDIRECT_PRODUCT_LIST`, lalu semua return redirect diganti menggunakan konstanta itu. Dengan cara ini, kode lebih mudah dirawat karena jika path berubah, saya cukup mengubah satu titik saja.

Kedua, terdapat method test kosong pada `ProductRepositoryTest` (rule `java:S1186`). Strategi perbaikannya adalah melengkapi implementasi method test agar benar-benar menguji perilaku repository, bukan dibiarkan kosong. Setelah dilengkapi, setiap test memiliki tujuan yang jelas, assertion yang relevan, dan tidak lagi memunculkan peringatan metode kosong.

### 2) Apakah implementasi sekarang sudah memenuhi definisi CI dan CD?

Menurut saya, implementasi saat ini sudah memenuhi prinsip Continuous Integration karena setiap perubahan pada branch utama dan pull request otomatis menjalankan pipeline build, test, linting/analisis kualitas, serta quality gate. Mekanisme ini membuat masalah cepat terdeteksi sebelum kode digabung atau dirilis, sehingga integrasi berlangsung konsisten.

Dari sisi Continuous Deployment, alur sekarang juga sudah tepat karena proses deploy dijalankan melalui workflow khusus setelah workflow CI selesai dan berstatus sukses. Artinya, rilis ke Heroku tidak berjalan untuk commit yang gagal di tahap integrasi. Selain itu, ada verifikasi pascadeploy untuk memastikan aplikasi benar-benar aktif, sehingga proses deploy bukan hanya otomatis, tetapi juga tervalidasi.
