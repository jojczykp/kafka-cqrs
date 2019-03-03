import React from "react";
import PropTypes from "prop-types";

const OutputRequest = ({ id, label, value }) => (
  <div className="output-request-group">
    <label htmlFor={id}>{label}</label>
    <textarea
      className="output-request-control"
      id={id}
      value={value}
      readOnly
      rows="5"
    />
  </div>
);

OutputRequest.propTypes = {
  id: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputRequest;
