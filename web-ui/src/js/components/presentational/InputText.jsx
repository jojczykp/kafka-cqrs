import React from "react";
import PropTypes from "prop-types";

const InputText = ({ id, label, value, handleChange }) => (
  <div className="input-text-group">
    <label htmlFor={id}>{label}</label>
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
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputText;
