import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";
import OutputTraffic from "../presentational/OutputTraffic.jsx";

class UpdateDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      id: "",
      author: "",
      text: "",
      request: "",
      response: ""
    };
    this.handleChange = this.handleChange.bind(this);
    this.onClick = this.onClick.bind(this);
  }

  handleChange(event) {
    alert("Update! " + event.target.id + " " + event.target.value)
  }

  onClick(event) {
    this.updateRequest()
    this.makeCall()
    this.updateResponse()
  }

  updateRequest() {
    var url = "http://backend.com"

    var headers = {
        'Content-Type': 'abc/update'
    }

    var body = {
        id: this.state.id,
        author: this.state.author,
        text: this.state.text
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
    const { id, author, text, request, response } = this.state;
    return (
      <span>
        <form id="update-document-form">
          <InputId
            id="update-document-id"
            label="Id:"
            value={id}
            handleChange={this.handleChange}
          />
          <InputAuthor
            id="update-document-author"
            label="Author:"
            value={author}
            handleChange={this.handleChange}
          />
          <InputText
            id="update-document-text"
            label="Text:"
            value={text}
            handleChange={this.handleChange}
          />
          <InputButton
            id="update-document-button"
            value="Update Document"
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          id="update-document-request"
          label="Request:"
          value={request}
        />
        <OutputTraffic
          id="update-document-response"
          label="Response:"
          value={response}
        />
      </span>
    );
  }
}

export default UpdateDocumentContainer;

const wrapper = document.getElementById("update-document-div");
wrapper ? ReactDOM.render(<UpdateDocumentContainer />, wrapper) : false;
