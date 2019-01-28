import React from "react";
import PropTypes from "prop-types";

const DeleteDocumentInputButton = ({ id, value, handleChange }) => (
  <div className="delete-document-input-button-group">
    <input
      type="button"
      className="delete-document-input-button-control"
      id={id}
      value={value}
      onChange={handleChange}
      required
    />
  </div>
);

DeleteDocumentInputButton.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default DeleteDocumentInputButton;
