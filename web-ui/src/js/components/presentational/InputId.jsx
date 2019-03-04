import React from "react";
import PropTypes from "prop-types";

const InputId = ({ id, value, handleChange }) => (
  <div className="input-id-group">
    <label htmlFor={id}>Id:</label>
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
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputId;
