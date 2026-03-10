### Muhammad Rizky Antariksa
### 2406495552
### Progjut A

## Reflection 1
## Clean Code Principles yang Diterapkan

### 1. Meaningful Names
Saya menggunakan nama yang jelas dan deskriptif untuk class, method, dan variable. Misalnya `ProductController`, `createProduct()`, `validateProduct()`, dan `productName`. Semua nama langsung menjelaskan fungsinya tanpa perlu membaca implementasinya.

### 2. Functions
Setiap method hanya memiliki satu tanggung jawab (Single Responsibility Principle). Method untuk validasi terpisah dari method untuk create atau edit. Panjang method juga dijaga agar tidak terlalu panjang dan mudah dibaca.

### 3. Don't Repeat Yourself (DRY)
Saya melakukan refactoring untuk menghilangkan code duplication. Awalnya validasi input ada di dua tempat (create dan edit), sekarang sudah di-extract ke satu method `validateProduct()` yang bisa dipanggil dari mana saja. Magic strings juga sudah diubah jadi constants.

### 4. Error Handling
Ada pengecekan null sebelum melakukan operasi edit atau delete. Input dari user juga divalidasi (nama tidak boleh kosong, quantity tidak boleh negatif). Error message ditampilkan ke user jika terjadi kesalahan.

### 5. Comments
Code sudah cukup self-explanatory sehingga tidak perlu banyak comment. Nama method dan variable yang jelas sudah cukup untuk menjelaskan maksud code.

---

## Secure Coding Practices yang Diterapkan

### 1. Input Data Validation
Semua input dari user divalidasi sebelum diproses. Product name harus diisi dan di-trim untuk menghindari input whitespace-only. Product quantity harus positif (tidak boleh negatif).

### 2. Output Data Encoding
Thymeleaf otomatis melakukan HTML escaping untuk mencegah XSS attack. Semua data yang ditampilkan ke user sudah aman dari injeksi script berbahaya.

### 3. Delete Confirmation
Untuk operasi delete yang bersifat permanen, ada konfirmasi JavaScript yang muncul sebelum data benar-benar dihapus. Ini mencegah user menghapus data secara tidak sengaja.

### 4. Null Safety
Sebelum melakukan operasi pada product, selalu dicek terlebih dahulu apakah product tersebut ada di database. Ini mencegah error NullPointerException.

---

## Kekurangan dan Improvement yang Bisa Dilakukan Kedepannya

### 1. Authentication & Authorization
Aplikasi saat ini tidak memiliki sistem login. Siapa saja bisa mengakses, mengedit, dan menghapus product. Seharusnya ditambahkan Spring Security untuk authentication dan role-based access control.

### 2. UUID Generation
Awalnya saya lupa menambahkan UUID generator saat create product, sehingga product ID tidak ter-generate dan menyebabkan error saat delete. Setelah diperbaiki dengan menambahkan UUID.randomUUID(), masalah teratasi.

### 3. Delete Method (GET vs POST)
Saya menggunakan GET request untuk delete, padahal best practice adalah menggunakan POST karena delete mengubah state aplikasi. Alasannya karena tanpa Spring Security, POST method menyebabkan error. Untuk production, seharusnya pakai POST dengan CSRF protection.

### 4. Error Messages
Error message masih generic dan dalam bahasa Inggris. Seharusnya dibuat lebih user-friendly, spesifik, dan dalam bahasa Indonesia. Tampilan error juga bisa diperbaiki dengan alert box yang lebih menarik.

### 5. Logging
Belum ada logging untuk tracking operasi CRUD. Seharusnya ditambahkan logger untuk mencatat siapa melakukan apa dan kapan, terutama untuk operasi yang sensitif seperti delete.

### 6. Unit Testing
Belum ada unit test sama sekali. Seharusnya dibuat unit test untuk Repository, Service, dan Controller layer untuk memastikan semua fungsi berjalan dengan benar dan mencegah regression bug.

---

## Git Workflow yang Diterapkan

Saya menggunakan Feature Branch Workflow dengan struktur:
- Branch `main`: Base code dengan fitur list product
- Branch `edit-product`: Fitur edit product saja
- Branch `delete-product`: Fitur delete product saja

Setiap fitur dikerjakan di branch terpisah, kemudian di-merge ke main setelah selesai. Ini membuat development lebih terorganisir dan mudah di-track. Saat merge terjadi conflict karena kedua branch sama-sama mengubah file yang sama, conflict di-resolve dengan memilih versi yang paling sesuai.

---

## Kesimpulan

Secara keseluruhan, code sudah menerapkan clean code principles seperti meaningful names, DRY, proper error handling, dan input validation. Untuk secure coding, sudah ada validasi input, output encoding, dan null safety. Namun masih ada beberapa kekurangan terutama di aspek security (belum ada authentication/authorization) dan quality assurance (belum ada unit testing). Untuk improvement selanjutnya, prioritas utama adalah menambahkan Spring Security dan unit testing.

---

## Reflection 2
Setelah menulis unit test, saya merasa lebih percaya diri terhadap fungsionalitas aplikasi karena setiap fitur utama diuji secara terpisah menggunakan skenario positif dan negatif. Jumlah unit test dalam satu class tidak memiliki batas pasti, namun sebaiknya cukup untuk mencakup seluruh perilaku penting dari class tersebut. Untuk memastikan unit test sudah memadai, code coverage dapat digunakan sebagai indikator sejauh mana kode telah diuji. Namun, meskipun code coverage mencapai 100%, hal tersebut tidak menjamin bahwa aplikasi bebas dari bug, karena masih mungkin terdapat kesalahan logika atau edge case yang tidak teruji.

Setelah menulis CreateProductFunctionalTest dan kemudian diminta membuat functional test lain untuk memverifikasi jumlah item pada product list, terlihat bahwa banyak kode setup yang harus ditulis ulang, seperti konfigurasi Selenium dan inisialisasi base URL. Hal ini membuat kode menjadi kurang bersih dan berpotensi menurunkan kualitas karena adanya duplikasi. Jika dibiarkan, perubahan kecil pada setup dapat menyebabkan banyak test perlu diperbarui. Untuk memperbaiki hal ini, setup yang sama sebaiknya diekstrak ke class dasar atau helper agar functional test lebih ringkas, mudah dibaca, dan lebih mudah dirawat ke depannya.

## Kesimpulan

Unit test dan functional test sangat membantu meningkatkan kepercayaan terhadap kualitas aplikasi. Code coverage berguna sebagai indikator awal, tetapi bukan jaminan bebas bug. Untuk functional test, menjaga kebersihan kode sama pentingnya dengan menjaga kebersihan kode aplikasi, terutama dengan menghindari duplikasi dan meningkatkan maintainability melalui reuse dan struktur yang baik.

---

## Reflection 3
## Prinsip solid yang diterapkan:

### 1. Single Responsibility Principle (SRP)
Memisahkan CarController dari ProductController. Sebelumnya, CarController adalah inner class di dalam ProductController, yang berarti satu file bertanggung jawab atas dua hal yang tidak berkaitan sama sekali. Ini melanggar SRP karena ProductController punya lebih dari satu alasan untuk berubah, yakni ia harus diubah jika routing Product berubah dan juga harus diubah jika routing Car berubah.

### 2. Open/Closed Principle (OCP)
Semua controller bergantung pada interface, bukan pada implementasi konkretnya. Artinya, behavior sistem bisa diperluas tanpa mengubah kode yang sudah ada. Misalnya, jika nantinya perlu ditambahkan sesuatu yang baru, ProductController tidak perlu diubah sama sekali, cukup buat implementasi baru dan daftarkan ke Spring.

### 3. Liskov Substitution Principle (LSP)
Pelanggaran LSP jelas terpampang di CarController extends ProductController. Sebuah Car controller bukan "jenis" dari Product controller, mereka adalah dua entitas yang sejajar dan independen. Dengan menghapus inheritance ini dan menjadikan CarController kelas mandiri, LSP terpenuhi karena tidak ada subclass yang melanggar kontrak superclass-nya.

### 4. Interface Segregation Principle (ISP)
ProductService dan CarService dipertahankan sebagai dua interface yang terpisah. ProductController hanya tahu tentang ProductService, dan CarController hanya tahu tentang CarService. Tidak ada yang dipaksa bergantung pada method yang tidak dibutuhkan. Selain itu, method deleteProductById yang duplikat dan kosong dihapus dari ProductService sehinnga interface menjadi lebih ringkas dan hanya berisi method yang benar-benar dibutuhkan.

### 5. Dependency Inversion Principle (DIP)
Sebelumnya, CarController langsung meng-inject CarServiceImpl dan bergantung pada implementasi konkret, bukan abstraksi. Setelah refactor, CarController bergantung pada interface CarService. Begitu pula semua controller lainnya, semuanya bergantung pada interface dan bukan pada kelas Impl-nya langsung. Selain itu, semua dependency diinjeksikan melalui constructor injection, bukan field injection, sehingga dependency lebih eksplisit dan mudah di-test.

## Keuntungan Menerapkan SOLID
### Mudah di-test secara terpisah
Karena ProductController bergantung pada interface ProductService, bukan langsung ke ProductServiceImpl, proses unit test jadi lebih mudah. Saat pengujian, ProductService bisa di-mock menggunakan Mockito tanpa perlu menjalankan implementasi aslinya. Hal ini terlihat pada ProductControllerTest, di mana ProductService di-mock sehingga pengujian benar-benar terisolasi dan tidak bergantung pada ProductRepository maupun komponen lain.

### Perubahan di satu bagian tidak merusak bagian lain
Dengan pemisahan antara CarController dan ProductController, perubahan pada logika Car, misalnya penambahan validasi input saat create car, hanya berdampak pada CarController dan CarServiceImpl. ProductController beserta seluruh test Product tetap berjalan normal tanpa terpengaruh.

### Mudah diperluas tanpa risiko
Penerapan prinsip Open–Closed (OCP) memungkinkan penambahan implementasi baru dari ProductService tanpa harus mengubah ProductController. Contohnya, jika ingin menambahkan service yang mengambil data dari database sungguhan, cukup membuat class baru yang mengimplementasikan ProductService. Controller tetap menggunakan interface yang sama.

## Kerugian Tidak Menerapkan SOLID
### Sulit di-test dan rentan error
Pada kode lama, CarController dibuat sebagai inner class yang mewarisi ProductController dan langsung meng-inject CarServiceImpl, bukan interface. Kondisi ini membuat unit test sulit dilakukan karena service tidak bisa di-mock dengan mudah. Dampaknya terlihat dari ProductControllerTest, di mana seluruh 11 test gagal karena keberadaan CarController sebagai inner class mengganggu konfigurasi MockMvc.

### Perubahan kecil berdampak besar
Karena CarController berada di dalam ProductController, setiap perubahan pada routing Car, meskipun tidak berkaitan dengan Product, tetap mengharuskan perubahan pada file ProductController.java. Hal ini meningkatkan risiko terjadinya bug pada fitur Product yang sebelumnya sudah berjalan dengan baik.

### Duplikasi yang membingungkan
Pada ProductService versi lama terdapat dua method dengan tujuan yang sama, yaitu delete(String id) dan deleteProductById(String productId). Method deleteProductById bahkan tidak memiliki implementasi dan tidak melakukan apa pun. Hal ini terjadi karena tidak adanya penerapan prinsip Interface Segregation (ISP), sehingga interface menjadi tidak jelas. Akibatnya, developer lain bisa salah memanggil method dan menyebabkan produk tidak terhapus tanpa adanya error, yang membuat bug sulit dilacak.

---

## Reflection 4
### 1. TDD Flow Usefulness
Setelah mengikuti alur Test-Driven Development (TDD) dalam tutorial ini, saya merefleksikan beberapa hal berdasarkan pertanyaan reflektif yang diusulkan oleh Percival (2017):
### Apakah saya menulis test sebelum kode produksi?
Ya, dalam tutorial ini saya mengikuti siklus RED-GREEN-REFACTOR. Setiap tahap dimulai dari menulis test yang gagal terlebih dahulu (RED), kemudian mengimplementasikan kode minimum agar test lulus (GREEN), dan terakhir melakukan refactor tanpa mengubah perilaku kode (REFACTOR).

### Apakah TDD cukup berguna?
Secara keseluruhan, alur TDD ini cukup berguna karena beberapa alasan:
### - Kejelasan requirement
Sebelum menulis kode, saya diminta memahami requirement secara mendalam. Misalnya, saya harus memahami bahwa status hanya boleh berisi 4 nilai valid dan products tidak boleh kosong sebelum mengimplementasikan Order model.
### - Desain yang lebih baik
Dengan menulis test terlebih dahulu, saya secara tidak langsung merancang API/interface dari kelas yang akan dibuat
### - Keamanan saat refactor
Ketika melakukan refactor, saya bisa memastikan bahwa perubahan tidak merusak fungsionalitas yang sudah ada karena test sudah tersedia.
### - Deteksi bug lebih awal
Beberapa edge case seperti status tidak valid atau produk kosong langsung terdeteksi sejak awal pengembangan.

### Hal yang perlu diperbaiki ke depannya
- Saya perlu melatih kebiasaan menulis test yang lebih ekspresif agar mudah dipahami.
- Saya perlu lebih disiplin dalam tidak menulis kode produksi sebelum ada test yang gagal.
- Cakupan test perlu diperluas, seperti menambahkan test untuk kasus-kasus boundary yang belum tercakup.

### 2. Kesesuaian Tests dengan Prinsip F.I.R.S.T.
### Fast (F)
Test yang dibuat sudah cukup cepat karena tidak melibatkan I/O eksternal seperti database sungguhan atau network call. Penggunaan Mockito untuk meng-mock OrderRepository di OrderServiceImplTest memastikan test berjalan dalam memori saja sehingga eksekusinya sangat cepat.
### Isolated/Independent (I)
Setiap test berjalan secara independen. Penggunaan @BeforeEach memastikan setiap test dimulai dari state yang bersih dan tidak bergantung pada hasil test lain. Mock pada layer service juga memastikan test tidak bergantung pada implementasi repository yang sebenarnya.
### Repeatable (R)
est dapat dijalankan berulang kali dengan hasil yang konsisten karena tidak ada dependensi terhadap kondisi eksternal.
### Self-Validating (S)
Semua test menggunakan assertion yang jelas sehingga hasilnya langsung terlihat PASS atau FAIL tanpa perlu interpretasi manual.
### Timely (T)
Test ditulis sebelum kode produksi sesuai TDD sehingga prinsip timely terpenuhi dengan baik.