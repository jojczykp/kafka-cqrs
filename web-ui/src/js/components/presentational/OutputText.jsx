import React from "react";
import PropTypes from "prop-types";

const OutputText = ({ id, label, value, handleChange }) => (
  <div className="output-text-group">
    <label htmlFor={id}>{label}</label>
    <textarea
      className="output-text-control"
      id={id}
      value={value}
      readOnly
    />
  </div>
);

OutputText.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputText;
