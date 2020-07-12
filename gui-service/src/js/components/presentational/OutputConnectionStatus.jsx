import React from "react";
import PropTypes from "prop-types";

function getClassName(readyState) {
  switch (readyState) {
    case EventSource.CONNECTING: return "output-connection-status-control-connecting";
    case EventSource.OPEN: return "output-connection-status-control-open";
    case EventSource.CLOSED: return "output-connection-status-control-closed";
    default: return "output-connection-status-control-paused"
  }
}

const OutputConnectionStatus = ({ id, value }) => (
  <span
    id={id}
    className={getClassName(value)}
  >
    &#x2B24;
  </span>
);

OutputConnectionStatus.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.number
};

export default OutputConnectionStatus;
