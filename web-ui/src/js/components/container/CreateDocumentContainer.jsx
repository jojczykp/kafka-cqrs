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
    this.handleAuthorChange = this.handleAuthorChange.bind(this);
    this.handleTextChange = this.handleTextChange.bind(this);
    this.onClick = this.onClick.bind(this);
  }

  handleAuthorChange(event) {
    this.state.author = event.target.value
    this.updateRequest()
  }

  handleTextChange(event) {
    this.state.text = event.target.value
    this.updateRequest()
  }

  onClick(event) {
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

  componentDidMount() {
    this.updateRequest()
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
            handleChange={this.handleAuthorChange}
          />
          <InputText
            id="create-document-text"
            label="Text:"
            value={text}
            handleChange={this.handleTextChange}
          />
          <InputButton
            id="create-document-button"
            value="Create Document"
            handleClick={this.onClick}
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