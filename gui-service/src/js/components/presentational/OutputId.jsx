import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { useSharedId } from "../../contexts/SharedIdContext.jsx";

const OutputId = ({ id, value }) => {
  const { setSharedId } = useSharedId();
  useEffect(() => {
    if (value) {
      setSharedId(value);
    }
  }, [value, setSharedId]);

  return (
    <div className="output-id-group">
      <label htmlFor={id}>Id:</label>
      <input
        type="text"
        className="output-id-control"
        id={id}
        value={value}
        readOnly
      />
    </div>
  );
};

OutputId.propTypes = {
  id: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
};

export default OutputId;
