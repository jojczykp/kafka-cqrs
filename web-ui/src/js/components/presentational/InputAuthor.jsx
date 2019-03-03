import React from "react";
import PropTypes from "prop-types";

const InputAuthor = ({ id, label, value, handleChange }) => (
  <div className="input-author-group">
    <label htmlFor={id}>{label}</label>
    <input
      type="text"
      className="input-author-control"
      id={id}
      value={value}
      onChange={handleChange}
      required
    />
  </div>
);

InputAuthor.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputAuthor;
