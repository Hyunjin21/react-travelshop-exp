import { useEffect, useContext, useState, useRef } from "react";
import ErrorBanner from "../../components/ErrorBanner";
import { OrderContext } from "../../contexts/OrderContext";
import api from "../../lib/api";

const STORAGE_KEY = "orderHistory";

const loadHistory = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : [];
  } catch {
    return [];
  }
};
const saveHistory = (list) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
};

function CompletePage({ setStep }) {
  const [OrderDatas, , resetOrderDatas] = useContext(OrderContext);
  const [orderHistory, setOrderHistory] = useState(() => loadHistory());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const calledRef = useRef(false);

  // 고유 userId (최초 1회 생성 후 재사용)
  const getUserId = () => {
    let uid = localStorage.getItem("userId");
    if (!uid) {
      uid = `guest-${Math.random().toString(36).slice(2, 10)}`;
      localStorage.setItem("userId", uid);
    }
    return uid;
  };
  const userId = getUserId();

  useEffect(() => {
    if (calledRef.current) return;
    calledRef.current = true;

    (async () => {
      try {
        await orderCompleted(OrderDatas);
      } catch (e) {
        console.error("order error:", e?.response?.status, e?.response?.data || e.message);
        setError(true);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  // 제목(이름)으로 productId 찾기
  const resolveProductIdByTitle = async (title) => {
    const { data } = await api.get("/products", { params: { q: title } });
    const match = data.find((p) => p.title === title) || data[0];
    if (!match?.id) throw new Error(`상품 ID를 찾을 수 없습니다: ${title}`);
    return match.id;
  };

  const orderCompleted = async (ctx) => {
    // 체크된 옵션 이름들
    const optionNames = Array.from(ctx.options.entries())
      .filter(([, v]) => (Number(v) || 0) > 0)
      .map(([k]) => k);

    // 수량 > 0인 상품
    const products = Array.from(ctx.products.entries())
      .map(([name, qty]) => ({ name, qty: Number(qty) || 0 }))
      .filter((x) => x.qty > 0);

    if (products.length === 0) {
      throw new Error("장바구니에 담긴 상품이 없습니다.");
    }

    // 장바구니 담기
    for (const { name, qty } of products) {
      const productId = await resolveProductIdByTitle(name);

      const params = new URLSearchParams();
      params.set("userId", userId);
      params.set("productId", String(productId));
      params.set("qty", String(qty));
      optionNames.forEach((opt) => params.append("options", opt));

      await api.post("/cart", null, { params });
    }

    // 주문 생성
    const { data: orderDto } = await api.post("/cart/order", null, {
      params: { userId },
    });

    // localStorage에 히스토리 추가 저장 후 화면 반영
    const newEntry = {
      orderNumber: orderDto?.id ?? Math.floor(Math.random() * 1_000_000),
      price: ctx.totals.total,
      orderedAt: orderDto?.orderedAt ?? new Date().toISOString(),
    };

    const next = [...loadHistory(), newEntry];
    saveHistory(next);
    setOrderHistory(next);
  };

  if (error) {
    return <ErrorBanner message="에러가 발생했습니다." />;
  }

  const orderTable = orderHistory.map((item, idx) => (
    <tr key={`${item.orderNumber}-${idx}`}>
      <td>{item.orderNumber}</td>
      <td>{item.price}</td>
    </tr>
  ));

  const handleClick = () => {
    resetOrderDatas();   // 장바구니 비우기
    setStep(0);          // 첫 페이지로
  };

  if (loading) {
    return <div>loading</div>;
  }

  return (
    <div style={{ textAlign: "center" }}>
      <h2>주문이 성공했습니다.</h2>
      <h3>지금까지 모든 주문</h3>
      <table style={{ margin: "auto" }}>
        <tbody>
          <tr>
            <th>주문 번호</th>
            <th>주문 가격</th>
          </tr>
          {orderTable}
        </tbody>
      </table>
      <button onClick={handleClick}>첫페이지로</button>
    </div>
  );
}

export default CompletePage;
