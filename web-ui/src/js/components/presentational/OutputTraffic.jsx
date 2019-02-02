import React from "react";
import PropTypes from "prop-types";

const OutputTraffic = ({ id, label, value }) => (
  <div className="update-document-output-traffic-group">
    <label htmlFor={id}>{label}</label>
    <div
      className="output-traffic-control"
      id={id}
    >
      {value}
    </div>
  </div>
);

OutputTraffic.propTypes = {
  label: PropTypes.string.isRequired,
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputTraffic;
