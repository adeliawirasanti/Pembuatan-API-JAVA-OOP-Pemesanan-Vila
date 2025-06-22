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

## ⚠️ Error Handling

API ini menggunakan sistem error handling terstruktur yang mengembalikan respons JSON standar ketika terjadi kesalahan. Setiap error response akan menyertakan:  
- Kode status HTTP yang jelas  
- Pesan error deskriptif  
- Timestamp untuk keperluan debugging  

Berikut adalah daftar error yang umum terjadi:

| Kode Error | Penyebab                  | Solusi                     |
|------------|---------------------------|----------------------------|
| 400        | Data tidak valid          | Periksa kelengkapan dan format data yang dikirim |
| 401        | API Key tidak valid       | Pastikan header `X-API-KEY` berisi kunci yang benar |
| 404        | Data tidak ditemukan      | Verifikasi ID yang diminta tersedia di database |

**Contoh Response Error**:
```json
{
  "status": "error",
  "code": 404,
  "message": "Data vila dengan ID 123 tidak ditemukan",
  "timestamp": "2023-11-22T10:15:30Z"
}

API dapat diuji menggunakan aplikasi Postman dengan mengirim request ke endpoint yang telah ditentukan. Database SQLite digunakan sebagai penyimpanan data, dengan skema yang mengikuti diagram relasional yang disediakan. Dengan fitur-fitur ini, API siap diintegrasikan dengan aplikasi frontend atau mobile untuk memfasilitasi pemesanan vila secara efisien.
