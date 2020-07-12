import React from "react";
import PropTypes from "prop-types";

const OutputId = ({ id, value }) => (
  <div className="output-id-group">
    <label htmlFor={id}>Id:</label>
    <input
      type="text"
      className="output-id-control"
      id={id}
      value={value}
      readOnly
    />
    <button disabled={!value} onClick={() => {
      navigator.clipboard.writeText(value);
    }}>
      &#x1F4CB;
    </button>
  </div>
);

OutputId.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputId;
