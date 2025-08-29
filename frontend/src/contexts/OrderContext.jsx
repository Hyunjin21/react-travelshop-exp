import { createContext, useState, useMemo, useEffect } from "react";

export const OrderContext = createContext();

const OPTION_PRICE = 5000;

function calculateSubtotal(orderType, orderCounts, productUnitPrices) {
  if (orderType === "products") {
    let sum = 0;
    for (const [name, qty] of orderCounts.products.entries()) {
      const unit = productUnitPrices.get(name) ?? 0;
      const count = Number(qty) || 0;
      sum += unit * count;
    }
    return sum;
  }

  // options: 체크된 개수 * 5,000
  let count = 0;
  for (const val of orderCounts.options.values()) {
    count += Number(val) || 0;
  }
  return count * OPTION_PRICE;
}

export function OrderContextProvider(props) {
  const [orderCounts, setOrderCounts] = useState({
    products: new Map(),
    options: new Map(),
  });

  // 상품별 단가를 보관
  const [productUnitPrices, setProductUnitPrices] = useState(new Map());

  const [totals, setTotals] = useState({
    products: 0,
    options: 0,
    total: 0,
  });

  useEffect(() => {
    const productsTotal = calculateSubtotal("products", orderCounts, productUnitPrices);
    const optionsTotal = calculateSubtotal("options", orderCounts, productUnitPrices);
    setTotals({
      products: productsTotal,
      options: optionsTotal,
      total: productsTotal + optionsTotal,
    });
  }, [orderCounts, productUnitPrices]);

  const value = useMemo(() => {
    function updateItemCount(itemName, newItemCount, orderType, unitPrice) {
      setOrderCounts(prev => {
        const next = {
          products: new Map(prev.products),
          options: new Map(prev.options),
        };
        const map = next[orderType];
        map.set(itemName, parseInt(newItemCount) || 0);
        return next;
      });

      if (orderType === "products" && unitPrice != null) {
        setProductUnitPrices(prev => {
          const next = new Map(prev);
          next.set(itemName, Number(unitPrice));
          return next;
        });
      }
    }

    const resetOrderDatas = () => {
      setOrderCounts({ products: new Map(), options: new Map() });
      setProductUnitPrices(new Map());
    };

    return [{ ...orderCounts, totals }, updateItemCount, resetOrderDatas];
  }, [orderCounts, totals]);

  return <OrderContext.Provider value={value} {...props} />;
}
