import React from "react";
import PropTypes from "prop-types";

const InputId = ({ id, label, value, handleChange }) => (
  <div className="update-document-input-id-group">
    <label htmlFor={id}>{label}</label>
    <input
      type="text"
      className="input-id-control"
      id={id}
      value={value}
      onChange={handleChange}
      required
    />
  </div>
);

InputId.propTypes = {
  id: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputId;
