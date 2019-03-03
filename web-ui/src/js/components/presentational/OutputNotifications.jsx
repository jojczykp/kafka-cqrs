import React from "react";
import PropTypes from "prop-types";

const OutputNotifications = ({ id, label, value }) => (
  <div className="output-notifications-group">
    <label htmlFor={id}>{label}</label>
    <div
      className="output-notifications-control"
      readOnly
    >
        {value.map((notification, index) =>
            <p key={index}>
                {notification}
            </p>
        )}
    </div>
  </div>
);

OutputNotifications.propTypes = {
  id: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  value: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default OutputNotifications;
