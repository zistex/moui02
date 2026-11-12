import { useState } from 'react';
import {
  Globe2,
  ArrowLeft,
  Building2,
  Calendar,
  CheckCircle2,
  Send,
  X,
  Sparkles,
  Search,
  Filter,
  DollarSign,
  Package,
  Layers,
  MapPin,
  Clock,
  ShieldCheck,
} from 'lucide-react';

interface ExportOpportunity {
  id: string;
  commodityName: string;
  category: string;
  buyerName: string;
  destinationCountry: string;
  destinationFlag: string;
  imageUrl: string;
  description: string;
  specifications: {
    grade: string;
    moistureOrQuality: string;
    packaging: string;
    certificationsRequired: string;
  };
  quantityRequired: string;
  targetPrice: string;
  targetPriceIdrEquivalent: string;
  incoterm: string;
  deadlineDays: number;
}

interface MobilePeluangEksporScreenProps {
  onBack?: () => void;
  isSubScreen?: boolean;
}

export default function MobilePeluangEksporScreen({
  onBack,
  isSubScreen = false,
}: MobilePeluangEksporScreenProps) {
  const [selectedCategory, setSelectedCategory] = useState<string>('Semua');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedOpportunity, setSelectedOpportunity] = useState<ExportOpportunity | null>(null);

  // Form submission state
  const [offerVolume, setOfferVolume] = useState('');
  const [offerPrice, setOfferPrice] = useState('');
  const [originLocation, setOriginLocation] = useState('Aceh Tengah');
  const [readinessTime, setReadinessTime] = useState('Ready Stok Siap Kirim');
  const [contactPhone, setContactPhone] = useState('081234567890');
  const [submissionSuccessId, setSubmissionSuccessId] = useState<string | null>(null);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const opportunities: ExportOpportunity[] = [
    {
      id: 'EXP-01',
      commodityName: 'Kopi Arabika Gayo Specialty Grade 1',
      category: 'Pertanian & Kopi',
      buyerName: 'Hanseatic Coffee Roasters GmbH',
      destinationCountry: 'Hamburg, Jerman',
      destinationFlag: '🇩🇪',
      imageUrl: '/src/assets/images/terniva_ekspor_commodities_1794449097514.jpg',
      description:
        'Kebutuhan pasokan green beans Arabika single-origin Gayo untuk roastery di Jerman. Kualitas rasa konsisten, petik merah 100%, dan cupping score terverifikasi.',
      specifications: {
        grade: 'Specialty Grade 1 (Double Picked)',
        moistureOrQuality: 'Kadar Air Maksimal 12% · Defect < 4%',
        packaging: 'Karung Goni 60 kg + inner GrainPro Bag',
        certificationsRequired: 'Phytosanitary Certificate, Fairtrade / Organic, COO Form A',
      },
      quantityRequired: '20 Ton (1 Kontainer FCL 20ft)',
      targetPrice: 'USD $8.50 / Kg FOB',
      targetPriceIdrEquivalent: '± Rp 138.000 / Kg',
      incoterm: 'FOB Pelabuhan Belawan Medan',
      deadlineDays: 4,
    },
    {
      id: 'EXP-02',
      commodityName: 'Vanili Kering Gourmet Planifolia Alor Grade A',
      category: 'Rempah & Herbal',
      buyerName: 'Al-Madina Gourmet Spices LLC',
      destinationCountry: 'Dubai, UAE',
      destinationFlag: '🇦🇪',
      imageUrl: '/src/assets/images/terniva_ekspor_commodities_1794449097514.jpg',
      description:
        'Vanili polong basah berminyak panjang 18-22 cm tanpa belah, aroma wangi vanillin murni untuk jaringan bakery mewah dan hotel di Uni Emirat Arab.',
      specifications: {
        grade: 'Gourmet Grade A (Panjang 18-22 cm)',
        moistureOrQuality: 'Kadar Vanillin > 2.2% · Kadar Air 30-33%',
        packaging: 'Vacuum Pack 5 Kg dalam Master Carton Box',
        certificationsRequired: 'Sertifikat Halal BPJPH, Phytosanitary, Lab Analysis',
      },
      quantityRequired: '3 Ton',
      targetPrice: 'USD $180.00 / Kg CIF',
      targetPriceIdrEquivalent: '± Rp 2.920.000 / Kg',
      incoterm: 'CIF Jebel Ali Port Dubai',
      deadlineDays: 6,
    },
    {
      id: 'EXP-03',
      commodityName: 'Jahe Gajah Segar & Kunyit Kuning Organik',
      category: 'Rempah & Herbal',
      buyerName: 'Nippon Agro Health Co., Ltd',
      destinationCountry: 'Kobe, Jepang',
      destinationFlag: '🇯🇵',
      imageUrl: '/src/assets/images/terniva_hero_landscape_1794449096818.jpg',
      description:
        'Rimpang jahe gajah segar dicuci bersih tanpa tanah untuk bahan baku industri farmasi herbal Jepang. Wajib bebas dari residu pestisida kimia.',
      specifications: {
        grade: 'Fresh Export Grade (Bobot rimpang ≥ 250 gram)',
        moistureOrQuality: 'Bebas Jamur & Bebas Residu Pestisida Kimia',
        packaging: 'Plastic Crates 20 Kg berventilasi (Air Cargo / Reefer)',
        certificationsRequired: 'MAFF Japan Quarantine Standards & Lab Test Non-GMO',
      },
      quantityRequired: '40 Ton (2 Kontainer Reefer 40ft)',
      targetPrice: 'USD $2.20 / Kg FOB',
      targetPriceIdrEquivalent: '± Rp 35.500 / Kg',
      incoterm: 'FOB Pelabuhan Tanjung Priok Jakarta',
      deadlineDays: 9,
    },
    {
      id: 'EXP-04',
      commodityName: 'Domba & Kambing Potong Hidup Standar Halal',
      category: 'Peternakan',
      buyerName: 'Singa Halal Meats Pte Ltd',
      destinationCountry: 'Jurong Port, Singapura',
      destinationFlag: '🇸🇬',
      imageUrl: '/src/assets/images/terniva_mitra_farmer_1794449097085.jpg',
      description:
        'Pengadaan ternak domba/kambing jantan sehat hidup untuk pasar pemotongan halal Singapura. Menjalani masa karantina 14 hari resmi di sentra karantina.',
      specifications: {
        grade: 'Jantan Sehat (Bobot Timbang Hidup 35-45 Kg)',
        moistureOrQuality: 'Bebas PMK, Antraks, & Brucellosis (Vaksin Lengkap)',
        packaging: 'Livestock Vessel Transport berstandar kesejahteraan hewan',
        certificationsRequired: 'SKKH Karantina RI & Singapore SFA Import Permit',
      },
      quantityRequired: '600 Ekor',
      targetPrice: 'SGD $320 / Ekor CIF',
      targetPriceIdrEquivalent: '± Rp 3.800.000 / Ekor',
      incoterm: 'CIF Jurong Port Singapore',
      deadlineDays: 3,
    },
    {
      id: 'EXP-05',
      commodityName: 'Briket Arang Tempurung Kelapa (Shisha Cube)',
      category: 'Olahan Kelapa',
      buyerName: 'Sultan Hookah Trading Group',
      destinationCountry: 'Riyadh, Arab Saudi',
      destinationFlag: '🇸🇦',
      imageUrl: '/src/assets/images/banner_hero_agritech_1794441148053.jpg',
      description:
        'Briket arang batok kelapa 100% alami tanpa bau kimia, tidak berasap, abu putih bersih, khusus untuk kafe dan lounge shisha di Timur Tengah.',
      specifications: {
        grade: 'Cube Premium 25 x 25 x 25 mm',
        moistureOrQuality: 'Abu Putih < 2.5% · Kalori 7.200 Kcal · Bakar > 2 Jam',
        packaging: 'Inner Plastic 1 Kg + Inner Box + Master Box 10 Kg',
        certificationsRequired: 'Self Heating Certificate (SHT) & MSDS Kontainer',
      },
      quantityRequired: '50 Ton (2 Kontainer 40ft)',
      targetPrice: 'USD $1.350 / Ton FOB',
      targetPriceIdrEquivalent: '± Rp 21.800 / Kg',
      incoterm: 'FOB Tanjung Perak Surabaya',
      deadlineDays: 12,
    },
  ];

  const categories = ['Semua', 'Pertanian & Kopi', 'Rempah & Herbal', 'Peternakan', 'Olahan Kelapa'];

  const filteredOpportunities = opportunities.filter((op) => {
    const matchCat = selectedCategory === 'Semua' || op.category === selectedCategory;
    const matchQuery =
      !searchQuery ||
      op.commodityName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      op.buyerName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      op.destinationCountry.toLowerCase().includes(searchQuery.toLowerCase());
    return matchCat && matchQuery;
  });

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 2500);
  };

  const handleOpenOfferModal = (op: ExportOpportunity) => {
    setSelectedOpportunity(op);
    setOfferVolume(op.quantityRequired.split(' ')[0] || '10');
    setOfferPrice(op.targetPrice);
    setSubmissionSuccessId(null);
  };

  const handleSubmitOffer = (e: React.FormEvent) => {
    e.preventDefault();
    if (!offerVolume || !offerPrice) return;
    const refCode = `EXP-${Math.floor(100000 + Math.random() * 900000)}`;
    setSubmissionSuccessId(refCode);
    showToast(`Pengajuan pasokan berhasil dikirim ke buyer! Ref: ${refCode}`);
  };

  return (
    <div className="bg-stone-100 min-h-full pb-10 select-none">
      {/* 1. Header (No rounded) */}
      <div className="bg-white border-b border-stone-200 px-3 py-2.5 flex items-center justify-between sticky top-0 z-20 shadow-xs">
        <div className="flex items-center gap-2 min-w-0">
          {isSubScreen && onBack && (
            <button
              onClick={onBack}
              className="p-1 text-stone-700 hover:text-emerald-800"
              title="Kembali"
            >
              <ArrowLeft className="w-5 h-5 stroke-[2.2]" />
            </button>
          )}
          <div className="min-w-0">
            <h3 className="text-sm font-bold text-stone-900 leading-tight flex items-center gap-1.5">
              <Globe2 className="w-4 h-4 text-emerald-800" />
              <span>Peluang Ekspor Komoditas</span>
            </h3>
            <span className="text-[10px] text-stone-500 font-mono">
              Permintaan Pasokan Langsung dari Buyer Mancanegara
            </span>
          </div>
        </div>

        <span className="bg-emerald-800 text-white text-[10px] font-bold px-2 py-0.5 font-mono">
          {opportunities.length} Tender Aktif
        </span>
      </div>

      {/* Floating Toast Notification */}
      {toastMessage && (
        <div className="fixed top-14 left-1/2 -translate-x-1/2 z-50 bg-stone-900 text-white text-xs px-3.5 py-1.5 border border-stone-700 shadow-xl flex items-center gap-1.5 animate-fade-in pointer-events-none">
          <Sparkles className="w-3.5 h-3.5 text-amber-300" />
          <span>{toastMessage}</span>
        </div>
      )}

      {/* 2. Top Banner Ringkas: Peluang Ekspor */}
      <div className="bg-emerald-900 text-white p-3.5 border-b border-emerald-950">
        <span className="text-[9px] font-bold text-emerald-300 uppercase tracking-widest block">
          Tender Terbuka Buyer Global
        </span>
        <h4 className="text-sm font-bold text-white mt-0.5">
          Pasok Hasil Panen & Ternak ke Pasar Internasional
        </h4>
        <p className="text-[11px] text-stone-200 mt-1 leading-snug">
          Buyer telah menetapkan kuantitas (Qty), harga target, dan spesifikasi standar. Anda cukup pilih
          komoditas yang sesuai dan ajukan pasokan.
        </p>
      </div>

      {/* 3. Category Filter Chips (No rounded) */}
      <div className="bg-white border-b border-stone-200 p-2.5">
        <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar">
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-3 py-1.5 text-xs font-semibold whitespace-nowrap border transition-colors ${
                selectedCategory === cat
                  ? 'bg-emerald-800 text-white border-emerald-800 font-bold'
                  : 'bg-stone-50 text-stone-700 border-stone-300 hover:border-emerald-800'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </div>

      {/* 4. Listing Komoditas yang Diperlukan oleh Buyer */}
      <div className="p-3 space-y-3">
        {filteredOpportunities.map((op) => (
          <div
            key={op.id}
            className="bg-white border border-stone-300 shadow-xs flex flex-col justify-between"
          >
            {/* Top Image + Buyer Info */}
            <div>
              <div className="relative aspect-[16/8] bg-stone-900 overflow-hidden">
                <img
                  src={op.imageUrl}
                  alt={op.commodityName}
                  referrerPolicy="no-referrer"
                  className="w-full h-full object-cover brightness-90"
                />

                {/* Country Flag & Destination Pill */}
                <div className="absolute top-2 left-2 bg-stone-950/85 text-white text-[10px] font-bold px-2 py-0.5 border border-stone-700 flex items-center gap-1.5">
                  <span className="text-xs">{op.destinationFlag}</span>
                  <span>{op.destinationCountry}</span>
                </div>

                {/* Deadline Tag */}
                <div className="absolute top-2 right-2 bg-rose-600 text-white text-[9px] font-bold px-2 py-0.5 flex items-center gap-1">
                  <Clock className="w-2.5 h-2.5" />
                  <span>Sisa {op.deadlineDays} Hari</span>
                </div>

                <div className="absolute bottom-2 left-2 bg-emerald-800 text-white text-[9px] font-bold px-2 py-0.5">
                  {op.category}
                </div>
              </div>

              {/* Core Content */}
              <div className="p-3 space-y-2.5">
                <div>
                  <h4 className="text-xs font-bold text-stone-900 leading-snug">
                    {op.commodityName}
                  </h4>
                  <div className="text-[10px] text-stone-500 flex items-center gap-1 mt-0.5">
                    <Building2 className="w-3 h-3 text-stone-400" />
                    <span>Buyer: <strong className="text-stone-800">{op.buyerName}</strong></span>
                  </div>
                </div>

                {/* Description */}
                <p className="text-xs text-stone-700 leading-snug bg-stone-50 p-2 border border-stone-200">
                  {op.description}
                </p>

                {/* Specifications Grid */}
                <div className="border border-stone-200 divide-y divide-stone-100 text-xs bg-stone-50/50">
                  <div className="p-1.5 px-2 flex justify-between">
                    <span className="text-[10px] text-stone-500">Standar Mutu:</span>
                    <span className="text-[10px] font-bold text-stone-800">{op.specifications.grade}</span>
                  </div>
                  <div className="p-1.5 px-2 flex justify-between">
                    <span className="text-[10px] text-stone-500">Kualitas Teknis:</span>
                    <span className="text-[10px] font-semibold text-stone-700 text-right">
                      {op.specifications.moistureOrQuality}
                    </span>
                  </div>
                  <div className="p-1.5 px-2 flex justify-between">
                    <span className="text-[10px] text-stone-500">Kemasan Ekspor:</span>
                    <span className="text-[10px] font-semibold text-stone-700">
                      {op.specifications.packaging}
                    </span>
                  </div>
                  <div className="p-1.5 px-2 flex justify-between">
                    <span className="text-[10px] text-stone-500">Sertifikasi:</span>
                    <span className="text-[10px] font-bold text-emerald-800 text-right truncate ml-2">
                      {op.specifications.certificationsRequired}
                    </span>
                  </div>
                </div>

                {/* Qty & Price Strip */}
                <div className="grid grid-cols-2 gap-2 bg-stone-100 p-2 border border-stone-200 text-xs">
                  <div>
                    <span className="text-[10px] text-stone-500 block">Kuantitas Dibutuhkan:</span>
                    <span className="font-bold text-stone-900 font-mono text-xs">
                      {op.quantityRequired}
                    </span>
                  </div>
                  <div>
                    <span className="text-[10px] text-stone-500 block">Harga Target Buyer:</span>
                    <span className="font-bold text-emerald-800 font-mono text-xs block">
                      {op.targetPrice}
                    </span>
                    <span className="text-[9px] text-stone-500 font-mono">
                      {op.targetPriceIdrEquivalent}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            {/* Bottom Action: "Ajukan ke Peluang Tersebut" */}
            <div className="p-3 pt-0">
              <button
                onClick={() => handleOpenOfferModal(op)}
                className="w-full py-2.5 bg-emerald-800 hover:bg-emerald-900 text-white font-bold text-xs flex items-center justify-center gap-1.5 shadow-xs transition-colors"
              >
                <span>Ajukan Pasokan ke Peluang Ini</span>
                <Send className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* 5. Modal Pengajuan Pasokan ke Buyer (No rounded) */}
      {selectedOpportunity && (
        <div className="fixed inset-0 z-50 bg-black/75 flex items-end sm:items-center justify-center p-0 sm:p-4">
          <div className="w-full max-w-[412px] bg-white text-stone-900 max-h-[90vh] flex flex-col shadow-2xl border border-stone-300">
            {/* Modal Header */}
            <div className="p-3 bg-emerald-900 text-white flex items-center justify-between shrink-0">
              <div className="flex items-center gap-2">
                <Globe2 className="w-4 h-4 text-emerald-300" />
                <div>
                  <h4 className="text-xs font-bold leading-tight">Pengajuan Pasokan Ekspor</h4>
                  <span className="text-[10px] text-emerald-200">
                    Buyer: {selectedOpportunity.buyerName} ({selectedOpportunity.destinationCountry})
                  </span>
                </div>
              </div>
              <button
                onClick={() => setSelectedOpportunity(null)}
                className="p-1 text-white/80 hover:text-white"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Modal Body */}
            <div className="flex-1 overflow-y-auto p-3.5 space-y-3">
              {/* Summary Box */}
              <div className="p-2.5 bg-stone-50 border border-stone-200 flex items-center gap-2.5">
                <img
                  src={selectedOpportunity.imageUrl}
                  alt={selectedOpportunity.commodityName}
                  className="w-12 h-12 object-cover bg-stone-200 shrink-0 border border-stone-300"
                />
                <div className="min-w-0 flex-1 text-xs">
                  <div className="font-bold text-stone-900 truncate">
                    {selectedOpportunity.commodityName}
                  </div>
                  <div className="text-[10px] text-stone-500">
                    Permintaan: <strong>{selectedOpportunity.quantityRequired}</strong>
                  </div>
                  <div className="text-emerald-800 font-mono font-bold text-[11px] mt-0.5">
                    Target: {selectedOpportunity.targetPrice}
                  </div>
                </div>
              </div>

              {submissionSuccessId ? (
                /* Success View */
                <div className="p-4 bg-emerald-50 border border-emerald-300 text-center space-y-2">
                  <CheckCircle2 className="w-10 h-10 text-emerald-700 mx-auto" />
                  <h4 className="text-sm font-bold text-stone-900">
                    Pengajuan Pasokan Berhasil Dikirim!
                  </h4>
                  <div className="p-2 bg-white border border-emerald-200 font-mono font-bold text-xs text-emerald-800">
                    No. Ref: {submissionSuccessId}
                  </div>
                  <p className="text-xs text-stone-600 leading-relaxed">
                    Pengajuan Anda sebesar <strong>{offerVolume}</strong> dengan harga{' '}
                    <strong>{offerPrice}</strong> telah diteruskan ke perwakilan buyer{' '}
                    <strong>{selectedOpportunity.buyerName}</strong>.
                  </p>
                  <p className="text-[11px] text-stone-500 leading-tight">
                    Tim <em>Terniva Trade Desk</em> akan menghubungi nomor {contactPhone} untuk
                    pengambilan sampel uji laboratorium & penandatanganan kontrak suplai.
                  </p>
                  <button
                    onClick={() => setSelectedOpportunity(null)}
                    className="w-full mt-2 py-2 bg-emerald-800 text-white font-bold text-xs"
                  >
                    Tutup & Lihat Peluang Lain
                  </button>
                </div>
              ) : (
                /* Input Form */
                <form onSubmit={handleSubmitOffer} className="space-y-3 text-xs">
                  <div>
                    <label className="text-[11px] font-bold text-stone-800 block mb-1">
                      Kapasitas Volume Siap Pasok:
                    </label>
                    <div className="flex items-center gap-1.5">
                      <input
                        type="text"
                        required
                        placeholder="Contoh: 10 Ton atau 500 Kg"
                        value={offerVolume}
                        onChange={(e) => setOfferVolume(e.target.value)}
                        className="flex-1 px-3 py-2 bg-stone-50 border border-stone-300 focus:outline-none focus:border-emerald-800 font-medium"
                      />
                    </div>
                    <span className="text-[9px] text-stone-400">
                      Target kebutuhan buyer: {selectedOpportunity.quantityRequired}
                    </span>
                  </div>

                  <div>
                    <label className="text-[11px] font-bold text-stone-800 block mb-1">
                      Harga Penawaran Anda:
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="Contoh: USD $8.40 / Kg atau Rp 135.000 / Kg"
                      value={offerPrice}
                      onChange={(e) => setOfferPrice(e.target.value)}
                      className="w-full px-3 py-2 bg-stone-50 border border-stone-300 focus:outline-none focus:border-emerald-800 font-medium"
                    />
                    <span className="text-[9px] text-stone-400">
                      Target beli buyer: {selectedOpportunity.targetPrice}
                    </span>
                  </div>

                  <div>
                    <label className="text-[11px] font-bold text-stone-800 block mb-1">
                      Lokasi Kebun / Gudang Pasokan:
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="Kabupaten / Provinsi"
                      value={originLocation}
                      onChange={(e) => setOriginLocation(e.target.value)}
                      className="w-full px-3 py-2 bg-stone-50 border border-stone-300 focus:outline-none focus:border-emerald-800 font-medium"
                    />
                  </div>

                  <div>
                    <label className="text-[11px] font-bold text-stone-800 block mb-1">
                      Estimasi Kesiapan Pengiriman:
                    </label>
                    <select
                      value={readinessTime}
                      onChange={(e) => setReadinessTime(e.target.value)}
                      className="w-full px-3 py-2 bg-stone-50 border border-stone-300 focus:outline-none focus:border-emerald-800 font-medium"
                    >
                      <option value="Ready Stok Siap Kirim (1-3 hari)">
                        Ready Stok Siap Kirim (1-3 hari)
                      </option>
                      <option value="Siap Panen dalam 7 Hari">Siap Panen dalam 7 Hari</option>
                      <option value="Siap Panen dalam 14 Hari">Siap Panen dalam 14 Hari</option>
                      <option value="Kontrak Pasokan Rutin Bulanan">
                        Kontrak Pasokan Rutin Bulanan
                      </option>
                    </select>
                  </div>

                  <div>
                    <label className="text-[11px] font-bold text-stone-800 block mb-1">
                      Nomor WhatsApp Kontak (Penanggung Jawab):
                    </label>
                    <input
                      type="tel"
                      required
                      placeholder="08xxxxxxxxxx"
                      value={contactPhone}
                      onChange={(e) => setContactPhone(e.target.value)}
                      className="w-full px-3 py-2 bg-stone-50 border border-stone-300 focus:outline-none focus:border-emerald-800 font-medium font-mono"
                    />
                  </div>

                  <div className="bg-stone-50 p-2 border border-stone-200 text-[10px] text-stone-500 leading-tight">
                    🛡️ <strong>Jaminan Transaksi Terniva:</strong> Kontrak ekspor difasilitasi dengan
                    Letter of Credit (L/C) dan pembayaran bertahap dijamin escrow bank nasional.
                  </div>

                  <button
                    type="submit"
                    className="w-full py-2.5 bg-emerald-800 hover:bg-emerald-900 text-white font-bold text-xs flex items-center justify-center gap-1.5 transition-colors shadow-sm"
                  >
                    <span>Kirim Pengajuan Pasokan ke Buyer</span>
                    <Send className="w-3.5 h-3.5" />
                  </button>
                </form>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
