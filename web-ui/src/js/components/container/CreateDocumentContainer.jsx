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
    alert("Create! " + event.target.id + " " + event.target.value + " " + state.author)
  }

  onClick(event) {
    this.setState({ request: this.state.request + "x", response: this.state.response + "y" });
  }

  render() {
    const { author, text, request, response } = this.state;
    return (
      <span>
        <InputAuthor
          id="create-document-author"
          label="Author:"
          value={author}
          handleChange={this.handleChange}
        />
        <InputText
          id="create-document-title"
          label="Text:"
          value={text}
          handleChange={this.handleChange}
        />
        <form id="create-document-form">
          <InputButton
            id="create-document-button"
            value="Create Document"
            onClick={this.onClick}
          />
        </form>
        <OutputTraffic
          id="create-document-request"
          label="Request:"
          value={request}
        />
        <OutputTraffic
          id="create-document-response"
          label="Response:"
          value={response}
        />
      </span>
    );
  }
}

export default CreateDocumentContainer;

const wrapper = document.getElementById("create-document-div");
wrapper ? ReactDOM.render(<CreateDocumentContainer />, wrapper) : false;