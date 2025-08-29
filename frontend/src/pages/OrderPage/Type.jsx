import React, { useEffect, useState, useContext } from "react";
import api from "../../lib/api";
import Products from "./Products";
import Options from "./Options";
import ErrorBanner from "../../components/ErrorBanner";
import { OrderContext } from "../../contexts/OrderContext";

function Type({ orderType }) {
  const [items, setItems] = useState([]);
  const [error, setError] = useState(false);
  const [orderDatas, updateItemCount] = useContext(OrderContext);

  useEffect(() => {
    loadItems(orderType);
  }, [orderType]);

  const loadItems = async (orderType) => {
    try {
      const endpoint = orderType === "products" ? "/products" : "/options";
      const { data } = await api.get(endpoint);
      setItems(data);
      setError(false);
    } catch (err) {
      setError(true);
    }
  };

  if (error) {
    return <ErrorBanner message="에러가 발생했습니다." />;
  }

  const ItemComponents = orderType === "products" ? Products : Options;

  const optionItems = items.map((item) => {
    const name = orderType === "products" ? item.title : item.name;
    const imagePath = orderType === "products" ? item.imagePath : undefined;
    const price = orderType === "products" ? item.price : undefined;

    return (
      <ItemComponents
        key={orderType === "products" ? item.id : item.name}
        name={name}
        imagePath={imagePath}
        updateItemCount={(itemName, newItemCount) =>
          updateItemCount(
            itemName,
            newItemCount,
            orderType,
            orderType === "products" ? price : undefined
          )
        }
      />
    );
  });

  const orderTypeKorean = orderType === "products" ? "상품" : "옵션";
  return (
    <>
      <h2>주문 종류</h2>
      <p>하나의 가격</p>
      <p>
        {orderTypeKorean} 총 가격: {orderDatas.totals[orderType]}
      </p>
      <div
        style={{
          display: "flex",
          flexDirection: orderType === "options" && "column",
        }}
      >
        {optionItems}
      </div>
    </>
  );
}

export default Type;
