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
      request: "",
      response: ""
    };
    this.handleChange = this.handleChange.bind(this);
    this.onClick = this.onClick.bind(this);
  }

  handleChange(event) {
    alert("Read! " + event.target.id + " " + event.target.value)
  }

  onClick(event) {
    this.setState({ request: this.state.request + "x", response: this.state.response + "y" });
  }

  render() {
    const { id, request, response } = this.state;
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
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          id="read-document-request"
          label="Request:"
          value={request}
        />
        <OutputTraffic
          id="read-document-response"
          label="Response:"
          value={response}
        />
      </span>
    );
  }
}

export default ReadDocumentContainer;

const wrapper = document.getElementById("read-document-div");
wrapper ? ReactDOM.render(<ReadDocumentContainer />, wrapper) : false;
