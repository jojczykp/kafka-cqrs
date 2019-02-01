import React from "react";
import PropTypes from "prop-types";

const InputButton = ({ id, value, onClick }) => (
  <div className="document-input-button-group">
    <input
      type="button"
      className="document-input-button-control"
      id={id}
      value={value}
      onClick={onClick}
      required
    />
  </div>
);

InputButton.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired
};

export default InputButton;
