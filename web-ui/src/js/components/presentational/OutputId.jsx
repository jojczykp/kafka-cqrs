import React from "react";
import PropTypes from "prop-types";

const OutputId = ({ id, label, value }) => (
  <div className="create-document-output-id-group">
    <label htmlFor={id}>{label}</label>
    <input
      type="text"
      className="output-id-control"
      id={id}
      value={value}
      readOnly
    />
  </div>
);

OutputId.propTypes = {
  id: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputId;
