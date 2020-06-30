import React from "react";
import PropTypes from "prop-types";

const InputText = ({ id, value, handleChange }) => (
  <div className="input-text-group">
    <label htmlFor={id}>Text:</label>
    <textarea
      className="input-text-control"
      id={id}
      value={value}
      onChange={handleChange}
      required
    />
  </div>
);

InputText.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputText;
