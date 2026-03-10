# Eshop (Spring Boot) 
Link : https://a-made-shandy-krisnanda-modul-7a19d7cc45a6.herokuapp.com
## Reflection

### 1) Refleksi alur TDD berdasarkan Percival (2017)
Menurut saya, alur Test-Driven Development (RED -> GREEN -> REFACTOR) sangat membantu untuk menjaga fokus implementasi dan menurunkan risiko regresi. Pada tahap RED, saya dipaksa merumuskan kebutuhan secara eksplisit melalui test case sebelum menulis kode produksi. Hal ini membuat batas perilaku sistem menjadi lebih jelas sejak awal, terutama pada kasus validasi status order, pembaruan data, dan skenario gagal.

Pada tahap GREEN, saya terdorong untuk menulis solusi minimum yang benar-benar dibutuhkan agar test lulus. Pendekatan ini menghindari over-engineering dan menjaga perubahan tetap kecil, sehingga lebih mudah ditinjau.

Tahap REFACTOR kemudian memberi ruang untuk memperbaiki kualitas desain tanpa mengubah perilaku. Contohnya, penggunaan `OrderStatus` untuk menggantikan string status yang tersebar membuat kode lebih konsisten dan mengurangi risiko typo.

Secara keseluruhan, alur TDD ini efektif dan relevan untuk proyek ini. Jika ke depan saya menemukan hambatan, perbaikan utama yang perlu saya lakukan adalah meningkatkan kualitas test di sisi edge case, memperjelas nama test agar lebih komunikatif, dan menjaga disiplin agar setiap commit benar-benar merepresentasikan satu fase TDD yang kecil dan terverifikasi.

### 2) Refleksi penerapan prinsip F.I.R.S.T. pada unit test
Secara umum, test yang saya buat sudah cukup sejalan dengan prinsip F.I.R.S.T.:

- Fast: Unit test berjalan cepat karena menggunakan in-memory object dan mocking pada dependency service.
- Independent: Setiap test memiliki setup yang jelas, tidak bergantung pada urutan eksekusi test lain.
- Repeatable: Hasil test konsisten ketika dijalankan berulang karena data awal dikontrol di masing-masing setup.
- Self-validating: Test menggunakan assertion yang eksplisit sehingga hasil lulus/gagal dapat diketahui otomatis.
- Timely: Test disusun sebelum implementasi pada alur TDD, sehingga perilaku di-definisikan lebih awal.

Walaupun demikian, masih ada ruang perbaikan. Beberapa perbaikan yang akan saya lakukan pada pembuatan test berikutnya adalah memperketat batasan satu perilaku per test, merapikan struktur test agar lebih ringkas, dan menambah cakupan skenario negatif yang belum terwakili secara memadai. Dengan perbaikan tersebut, kualitas test akan lebih kuat sebagai safety net saat refactor dan penambahan fitur baru.
