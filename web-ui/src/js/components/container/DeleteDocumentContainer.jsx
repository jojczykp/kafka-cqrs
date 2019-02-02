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
    this.handleIdChange = this.handleIdChange.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  handleIdChange(event) {
    this.state.id = event.target.value
    this.updateRequest()
  }

  handleClick(event) {
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
            handleChange={this.handleIdChange}
          />
          <InputButton
            id="delete-document-button"
            value="Delete Document"
            handleClick={this.handleClick}
          />
        </form>
        <OutputTraffic
          requestId="delete-document-request"
          requestLabel="Request:"
          requestValue={request}
          responseId="delete-document-response"
          responseLabel="Response:"
          responseValue={response}
        />
      </span>
    );
  }
}

export default DeleteDocumentContainer;

const wrapper = document.getElementById("delete-document-div");
wrapper ? ReactDOM.render(<DeleteDocumentContainer />, wrapper) : false;
