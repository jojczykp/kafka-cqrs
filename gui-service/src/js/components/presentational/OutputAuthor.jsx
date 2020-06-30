import React from "react";
import PropTypes from "prop-types";

const OutputAuthor = ({ id, value, handleChange }) => (
  <div className="output-author-group">
    <label htmlFor={id}>Author</label>
    <input
      type="text"
      className="output-author-control"
      id={id}
      value={value}
      readOnly
    />
  </div>
);

OutputAuthor.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputAuthor;
