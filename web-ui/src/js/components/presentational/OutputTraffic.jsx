import React from "react";
import PropTypes from "prop-types";

const OutputTraffic = ({
  requestId, requestLabel, requestValue,
  responseId, responseLabel, responseValue
}) => (
  <div className="output-traffic-group">
    <label htmlFor={requestId}>{requestLabel}</label>
    <div
      className="output-traffic-request-control"
      id={requestId}
    >
      {requestValue}
    </div>
    <label htmlFor={responseId}>{responseLabel}</label>
    <div
      className="output-traffic-response-control"
      id={responseId}
    >
      {responseValue}
    </div>
  </div>
);

OutputTraffic.propTypes = {
  requestId: PropTypes.string.isRequired,
  requestLabel: PropTypes.string.isRequired,
  requestValue: PropTypes.string.isRequired,
  responseId: PropTypes.string.isRequired,
  responseLabel: PropTypes.string.isRequired,
  responseValue: PropTypes.string.isRequired
};

export default OutputTraffic;
