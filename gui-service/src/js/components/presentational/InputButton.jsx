import React from "react";
import PropTypes from "prop-types";

const InputButton = ({ id, value, handleClick }) => (
  <span className="input-button-group">
    <input
      type="button"
      className="input-button-control"
      id={id}
      value={value}
      onClick={handleClick}
      required
    />
  </span>
);

InputButton.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleClick: PropTypes.func.isRequired
};

export default InputButton;
