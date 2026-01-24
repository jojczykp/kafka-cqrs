import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { useSharedId } from "../../contexts/SharedIdContext.jsx";

const InputId = ({ id, value, handleChange }) => {
  const { sharedId } = useSharedId();

  useEffect(() => {
    if (sharedId && sharedId !== value) {
      const syntheticEvent = {
        target: { value: sharedId }
      };
      handleChange(syntheticEvent);
    }
  }, [sharedId, value, handleChange]);

  return (
    <div className="input-id-group">
      <label htmlFor={id}>Id:</label>
      <input
        type="text"
        className="input-id-control"
        id={id}
        value={value}
        onChange={handleChange}
        required
      />
    </div>
  );
};

InputId.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handleChange: PropTypes.func.isRequired
};

export default InputId;
