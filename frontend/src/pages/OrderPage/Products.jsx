import React from "react";
import api from "../../lib/api";

function Products({ name, imagePath, updateItemCount }) {
  const handleChange = (event) => {
    const currentValue = event.target.value;
    updateItemCount(name, currentValue);
  };

  const isAbsolute = /^https?:\/\//i.test(api.defaults.baseURL || "");
  const imgSrc = isAbsolute
    ? new URL(imagePath || "", api.defaults.baseURL).href
    : (imagePath || "");

  return (
    <div style={{ textAlign: "center" }}>
      <img
        style={{ width: "75%" }}
        src={imgSrc}
        alt={`${name} product`}
      />
      <form style={{ marginTop: "10px" }}>
        <label htmlFor={name} style={{ textAlign: "right" }}>
          {name}
        </label>
        <input
          id={name}
          style={{ marginLeft: 7 }}
          type="number"
          name="quantity"
          min="0"
          defaultValue={0}
          onChange={handleChange}
        />
      </form>
    </div>
  );
}

export default Products;
