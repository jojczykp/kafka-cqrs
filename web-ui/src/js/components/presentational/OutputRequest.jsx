import React from "react";
import PropTypes from "prop-types";

const OutputRequest = ({ id, value }) => (
  <div className="output-request-group">
    <label htmlFor={id}>Request:</label>
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
  value: PropTypes.string.isRequired
};

export default OutputRequest;
