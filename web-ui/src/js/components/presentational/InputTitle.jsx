import React from "react";
import PropTypes from "prop-types";

const InputTitle = ({ id, label, value, handleChange }) => (
  <div className="update-document-input-title-group">
    <label htmlFor={id}>{label}</label>
    <input
      type="text"
      className="update-document-input-title-control"
      id={id}
      value={value}
      onChange={handleChange}
      required
    />
  </div>
);

InputTitle.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputTitle;
