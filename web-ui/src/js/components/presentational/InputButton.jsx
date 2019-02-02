import React from "react";
import PropTypes from "prop-types";

const InputButton = ({ id, value, handleClick }) => (
  <div className="document-input-button-group">
    <input
      type="button"
      className="document-input-button-control"
      id={id}
      value={value}
      onClick={handleClick}
      required
    />
  </div>
);

InputButton.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleClick: PropTypes.func.isRequired
};

export default InputButton;
