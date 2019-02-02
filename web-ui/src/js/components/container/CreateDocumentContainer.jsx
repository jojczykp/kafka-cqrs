import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class CreateDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      author: "",
      text: "",
      request: "",
      response: ""
    };
    this.handleChange = this.handleChange.bind(this);
    this.onClick = this.onClick.bind(this);
  }

  handleChange(event) {
    alert("Create! " + event.target.id + " " + event.target.value)
  }

  onClick(event) {
    this.updateRequest()
    this.makeCall()
    this.updateResponse()
  }

  updateRequest() {
    var url = "http://backend.com"

    var headers = {
        'Content-Type': 'abc/create'
    }

    var body = {
        author: this.state.author,
        text: this.state.text
    }

    this.setState({ request: JSON.stringify({ url, headers, body }, null, 4) });
  }

  makeCall() {
  }

  updateResponse() {
    var status = "201 Created"

    var headers = {
        'Content-Type': 'ghi/create'
    }

    var body = {
        id: 'some-new-id'
    }

    this.setState({ response: JSON.stringify({ status, headers, body }, null, 4) });
  }

  render() {
    const { author, text, request, response } = this.state;
    return (
      <span>
        <form id="create-document-form">
          <InputAuthor
            id="create-document-author"
            label="Author:"
            value={author}
            handleChange={this.handleChange}
          />
          <InputText
            id="create-document-text"
            label="Text:"
            value={text}
            handleChange={this.handleChange}
          />
          <InputButton
            id="create-document-button"
            value="Create Document"
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          requestId="create-document-request"
          requestLabel="Request:"
          requestValue={request}
          responseId="create-document-response"
          responseLabel="Response:"
          responseValue={response}
        />
      </span>
    );
  }
}

export default CreateDocumentContainer;

const wrapper = document.getElementById("create-document-div");
wrapper ? ReactDOM.render(<CreateDocumentContainer />, wrapper) : false;