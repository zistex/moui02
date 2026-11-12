export type MobileTab = 'home' | 'peluang' | 'live' | 'notif' | 'akun';

export type MobileScreen =
  | 'home'
  | 'mitra'
  | 'ekspor'
  | 'konsultasi'
  | 'product_detail'
  | 'cart'
  | 'checkout'
  | 'order_success';

export interface MobileProduct {
  id: string;
  name: string;
  category: string;
  price: number;
  originalPrice?: number;
  discountPercent?: number;
  rating: number;
  soldCount: number;
  location: string;
  supplier: string;
  stock: number;
  minOrder: string;
  description: string;
  imageUrl: string;
  isVerified: boolean;
}

export interface MobileCartItem {
  product: MobileProduct;
  quantity: number;
  isSelected: boolean;
}
