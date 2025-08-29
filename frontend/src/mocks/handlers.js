import { http, delay, HttpResponse } from "msw";

const PRODUCTS = [
  { id: 1, title: "America", imagePath: "/images/america.jpeg", price: 10000 },
  { id: 2, title: "England", imagePath: "/images/england.jpeg", price: 10000 },
];

const OPTIONS = [{ name: "Insurance" }, { name: "Dinner" }];

export const handlers = [
  // products
  http.get("*/api/products", () => {
    return HttpResponse.json(PRODUCTS);
  }),

  // options
  http.get("*/api/options", () => {
    return HttpResponse.json(OPTIONS);
  }),

  // 장바구니 담기
  http.post("*/api/cart", async ({ request }) => {
    const url = new URL(request.url);
    const userId = url.searchParams.get("userId") ?? "test-user";
    const productId = Number(url.searchParams.get("productId"));
    const qty = Number(url.searchParams.get("qty") ?? "1");
    const selectedOptions = url.searchParams.getAll("options");
    const product = PRODUCTS.find((p) => p.id === productId);

    return HttpResponse.json({
      id: 1,
      userId,
      items: product
        ? [
          {
            productId: product.id,
            title: product.title,
            quantity: qty,
            price: product.price, // 단가
            options: selectedOptions.map((name) => ({ name, price: 5000 })),
          },
        ]
        : [],
    });
  }),

  // 주문 확정 
  http.post("*/api/cart/order", async ({ request }) => {
    await delay(50);
    return HttpResponse.json([{ orderNumber: 1, price: 55000 }]);
  }),

];
