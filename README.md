# **✨ API Pemesanan Vila**  
<mark>Tugas 2 PBO- Sistem Manajemen Vila</mark> 

## **📌 Deskripsi Proyek** 
Program ini adalah sebuah API (Application Programming  Interface) yang dirancang untuk mengelola pemesanan vila. API ini menyediakan berbagai endpoint untuk melakukan operasi CRUD (Create, Read, Update, dan Delete) pada entitas-entitas seperti vila 🏠, pelanggan 👥, pemesanan 📅, dan lainnya. API dibangun dengan menggunakan bahasa pemrograman Java dan database SQLite untuk menyimpan data. Setiap request yang dikirim ke API harus disertai dengan API key yang valid untuk otentikasi, yang di hardcode dalam kelas Main sebagai lapisan keamanan dasar.

## **🔧 Fitur Utama**  
API ini mendukung beberapa jenis HTTP request method, yaitu :
* GET: Untuk mengambil daftar atau detail data entitas.
* POST: Untuk membuat entitas baru, dengan validasi kelengkapan dan format data (seperti email dan nomor telepon).
* PUT: Untuk memperbarui data entitas yang sudah ada.
* DELETE: Untuk menghapus data entitas.

Respons dari API diberikan dalam format JSON, baik untuk request GET maupun untuk body request POST/PUT. Jika terjadi kesalahan, seperti data tidak ditemukan (404) atau request tidak valid (400), API akan mengembalikan error response beserta pesan yang jelas. Error handling diimplementasikan menggunakan mekanisme exception untuk memastikan penanganan kesalahan yang terstruktur.

API dapat diuji menggunakan aplikasi Postman dengan mengirim request ke endpoint yang telah ditentukan. Database SQLite digunakan sebagai penyimpanan data, dengan skema yang mengikuti diagram relasional yang disediakan. Dengan fitur-fitur ini, API siap diintegrasikan dengan aplikasi frontend atau mobile untuk memfasilitasi pemesanan vila secara efisien.
