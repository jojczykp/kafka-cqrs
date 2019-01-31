import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputText from "../presentational/InputText.jsx";
import InputButton from "../presentational/InputButton.jsx";

class UpdateDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      id: "",
      author: "",
      text: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    this.setState({ [event.target.id]: event.target.value });
  }

  render() {
    const { id, author, text } = this.state;
    return (
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
          id="update-document-title"
          label="Text:"
          value={text}
          handleChange={this.handleChange}
        />
        <InputButton
          id="update-document-button"
          value="Update Document"
          handleChange={this.handleChange}
        />
      </form>
    );
  }
}

export default UpdateDocumentContainer;

const wrapper = document.getElementById("update-document-div");
wrapper ? ReactDOM.render(<UpdateDocumentContainer />, wrapper) : false;
