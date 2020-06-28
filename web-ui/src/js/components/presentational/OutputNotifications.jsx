import React from "react";
import PropTypes from "prop-types";

const OutputNotifications = ({ id, value }) => (
  <div className="output-notifications-group">
    <textarea
      className="output-notifications-control"
      readOnly
      rows="10"
      value={ value.join("\n") }
    />
  </div>
);

OutputNotifications.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default OutputNotifications;
