import React from "react";
import PropTypes from "prop-types";

const OutputNotifications = ({ id, value }) => (
  <div className="output-notifications-group">
    <textarea
      className="output-notifications-control"
      readOnly
      rows="10"
      value={ value
          .map((notification, index) => notification)
          .reduce((acc, curr, idx, arr) => acc.length == 0 ? curr : acc + "\n" + curr, "")}
    />
  </div>
);

OutputNotifications.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default OutputNotifications;
