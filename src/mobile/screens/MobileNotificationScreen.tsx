import { useState } from 'react';
import {
  Receipt,
  Sprout,
  Globe2,
  Stethoscope,
  Bell,
  CheckCheck,
  Circle,
  Inbox,
} from 'lucide-react';

interface NotificationItem {
  id: string;
  category: 'Transaksi' | 'Pantau Panen' | 'Ekspor' | 'Konsultasi' | 'Sistem';
  title: string;
  message: string;
  timeAgo: string;
  isRead: boolean;
}

export default function MobileNotificationScreen() {
  const [selectedCategory, setSelectedCategory] = useState<string>('Semua');
  const [notifications, setNotifications] = useState<NotificationItem[]>([
    {
      id: 'NOTIF-01',
      category: 'Transaksi',
      title: 'Pesanan Dibuat #TRN-98432101',
      message: 'Pesanan konsentrat pakan dan bibit Anda sedang dikirim oleh kurir JNE Agri Cargo Dingin.',
      timeAgo: '1 jam lalu',
      isRead: false,
    },
    {
      id: 'NOTIF-02',
      category: 'Pantau Panen',
      title: 'Laporan Mingguan: Sapi Limousin #TRN-SP-042',
      message: 'Bobot badan naik menjadi 312.4 Kg (ADG 1.35 Kg/hari). Suhu kandang 24.8°C dan kondisi sangat sehat.',
      timeAgo: '3 jam lalu',
      isRead: false,
    },
    {
      id: 'NOTIF-03',
      category: 'Ekspor',
      title: 'Peluang Baru: Permintaan Kopi Arabika ke Jerman',
      message: 'Buyer Hamburg mencari pasokan 20 Ton green beans specialty grade 1 dengan kontrak FOB Tanjung Priok.',
      timeAgo: '1 hari lalu',
      isRead: true,
    },
    {
      id: 'NOTIF-04',
      category: 'Konsultasi',
      title: 'Jawaban Dokter: Evaluasi Mastitis & Asam Urat Sapi',
      message: 'drh. Aryo Wibisono telah meresepkan multivitamin dan protokol dipping antiseptik ambing.',
      timeAgo: '2 hari lalu',
      isRead: true,
    },
    {
      id: 'NOTIF-05',
      category: 'Sistem',
      title: 'Verifikasi Akun Mitra Berhasil',
      message: 'Selamat! Akun kemitraan Anda telah diverifikasi oleh koordinator wilayah Subang Jawa Barat.',
      timeAgo: '3 hari lalu',
      isRead: true,
    },
  ]);

  const categories = ['Semua', 'Transaksi', 'Pantau Panen', 'Konsultasi', 'Ekspor', 'Sistem'];

  const filtered =
    selectedCategory === 'Semua'
      ? notifications
      : notifications.filter((n) => n.category.toLowerCase() === selectedCategory.toLowerCase());

  const markAllAsRead = () => {
    setNotifications((prev) => prev.map((n) => ({ ...n, isRead: true })));
  };

  const getCategoryIcon = (category: string) => {
    switch (category) {
      case 'Transaksi':
        return <Receipt className="w-4 h-4 text-amber-600" />;
      case 'Pantau Panen':
        return <Sprout className="w-4 h-4 text-emerald-600" />;
      case 'Ekspor':
        return <Globe2 className="w-4 h-4 text-sky-600" />;
      case 'Konsultasi':
        return <Stethoscope className="w-4 h-4 text-purple-600" />;
      default:
        return <Bell className="w-4 h-4 text-indigo-600" />;
    }
  };

  const getCategoryBg = (category: string) => {
    switch (category) {
      case 'Transaksi':
        return 'bg-amber-50 border-amber-200';
      case 'Pantau Panen':
        return 'bg-emerald-50 border-emerald-200';
      case 'Ekspor':
        return 'bg-sky-50 border-sky-200';
      case 'Konsultasi':
        return 'bg-purple-50 border-purple-200';
      default:
        return 'bg-indigo-50 border-indigo-200';
    }
  };

  return (
    <div className="bg-stone-100 min-h-full flex flex-col pb-8">
      {/* Header */}
      <div className="bg-white border-b border-stone-200 px-3 py-3 shadow-xs sticky top-0 z-20">
        <div className="flex items-center justify-between">
          <h3 className="text-sm font-bold text-stone-900">Pusat Notifikasi Terniva</h3>
          <button
            onClick={markAllAsRead}
            className="text-[11px] font-semibold text-emerald-800 hover:text-emerald-900 flex items-center gap-1"
          >
            <CheckCheck className="w-3.5 h-3.5" />
            <span>Tandai Semua Dibaca</span>
          </button>
        </div>

        {/* Category Filter Chips Horizontal */}
        <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar pt-2.5 pb-0.5">
          {categories.map((cat) => {
            const isSelected = selectedCategory === cat;
            return (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-3 py-1 rounded-full text-xs whitespace-nowrap transition-colors ${
                  isSelected
                    ? 'bg-emerald-800 text-white font-bold shadow-xs'
                    : 'bg-stone-100 text-stone-700 hover:bg-stone-200 font-medium'
                }`}
              >
                {cat}
              </button>
            );
          })}
        </div>
      </div>

      {/* Notification List */}
      <div className="p-3 space-y-2 flex-1">
        {filtered.length === 0 ? (
          <div className="text-center py-12 text-xs text-stone-500 space-y-2">
            <Inbox className="w-8 h-8 text-stone-300 mx-auto" />
            <div>Tidak ada notifikasi dalam kategori {selectedCategory}.</div>
          </div>
        ) : (
          filtered.map((item) => (
            <div
              key={item.id}
              onClick={() => {
                setNotifications((prev) =>
                  prev.map((n) => (n.id === item.id ? { ...n, isRead: true } : n))
                );
              }}
              className={`p-3 rounded-2xl border transition-all cursor-pointer ${
                item.isRead
                  ? 'bg-white border-stone-200 shadow-xs'
                  : 'bg-emerald-50/50 border-emerald-300 shadow-sm ring-1 ring-emerald-200'
              }`}
            >
              <div className="flex items-start gap-2.5">
                <div
                  className={`w-9 h-9 rounded-xl border flex items-center justify-center shrink-0 ${getCategoryBg(
                    item.category
                  )}`}
                >
                  {getCategoryIcon(item.category)}
                </div>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center justify-between gap-2">
                    <span className="text-[10px] font-bold text-stone-500 uppercase tracking-wider">
                      {item.category}
                    </span>
                    <span className="text-[10px] font-mono text-stone-400 shrink-0">
                      {item.timeAgo}
                    </span>
                  </div>

                  <h5 className="text-xs font-bold text-stone-900 mt-0.5 leading-snug">
                    {item.title}
                  </h5>

                  <p className="text-[11px] text-stone-600 leading-relaxed mt-1">
                    {item.message}
                  </p>
                </div>

                {!item.isRead && (
                  <span className="w-2 h-2 rounded-full bg-emerald-600 shrink-0 mt-1.5" />
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
