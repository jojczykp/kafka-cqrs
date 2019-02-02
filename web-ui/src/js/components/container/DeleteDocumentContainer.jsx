import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class DeleteDocumentContainer extends Component {
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
    alert("Delete! " + event.target.id + " " + event.target.value)
  }

  onClick(event) {
    this.updateRequest()
    this.makeCall()
    this.updateResponse()
  }

  updateRequest() {
    var url = "http://backend.com"

    var headers = {
        'Content-Type': 'abc/delete'
    }

    var body = {
        id: this.state.id
    }

    this.setState({ request: JSON.stringify({ url, headers, body }, null, 4) });
  }

  makeCall() {
  }

  updateResponse() {
    var status = "204 No Content"

    var headers = {
    }

    var body = {
    }

    this.setState({ response: JSON.stringify({ status, headers, body }, null, 4) });
  }

  render() {
    const { id, request, response } = this.state;
    return (
      <span>
        <form id="delete-document-form">
          <InputId
            id="delete-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleChange}
          />
          <InputButton
            id="delete-document-button"
            value="Delete Document"
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          id="delete-document-request"
          label="Request:"
          value={request}
        />
        <OutputTraffic
          id="delete-document-response"
          label="Response:"
          value={response}
        />
      </span>
    );
  }
}

export default DeleteDocumentContainer;

const wrapper = document.getElementById("delete-document-div");
wrapper ? ReactDOM.render(<DeleteDocumentContainer />, wrapper) : false;
