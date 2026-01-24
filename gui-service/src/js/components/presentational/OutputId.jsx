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
      const idInputs = document.querySelectorAll('input.input-id-control');
      idInputs.forEach(input => {
        input.value = value;
        const event = new Event('input', { bubbles: true });
        input.dispatchEvent(event);
      });
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
