import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class ReadDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      id: "",
      traffic: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    this.setState({ [event.target.id]: event.target.value });
  }

  render() {
    const { id, traffic } = this.state;
    return (
      <span>
        <form id="read-document-form">
          <InputId
            id="read-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleChange}
          />
          <InputButton
            id="read-document-button"
            value="Read Document"
            handleChange={this.handleChange}
          />
        </form>
        <OutputTraffic
          id="read-document-traffic"
          label="Traffic:"
          value={traffic}
        />
      </span>
    );
  }
}

export default ReadDocumentContainer;

const wrapper = document.getElementById("read-document-div");
wrapper ? ReactDOM.render(<ReadDocumentContainer />, wrapper) : false;
