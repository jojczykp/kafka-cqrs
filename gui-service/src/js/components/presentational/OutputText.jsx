import React from "react";
import PropTypes from "prop-types";

const OutputText = ({ id, value, handleChange }) => (
  <div className="output-text-group">
    <label htmlFor={id}>Text:</label>
    <textarea
      className="output-text-control"
      id={id}
      value={value}
      readOnly
    />
  </div>
);

OutputText.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputText;
