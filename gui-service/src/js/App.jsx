import React from "react";
import { createRoot } from "react-dom/client";
import { SharedIdProvider } from "./contexts/SharedIdContext.jsx";
import CreateDocumentContainer from "./components/container/CreateDocumentContainer.jsx";
import UpdateDocumentContainer from "./components/container/UpdateDocumentContainer.jsx";
import ReadDocumentContainer from "./components/container/ReadDocumentContainer.jsx";
import DeleteDocumentContainer from "./components/container/DeleteDocumentContainer.jsx";
import PictureContainer from "./components/container/PictureContainer.jsx";
import NotificationsContainer from "./components/container/NotificationsContainer.jsx";

const App = () => {
  return (
    <SharedIdProvider>
      <div className="container">
        <div id="create-document-div">
          <CreateDocumentContainer />
        </div>
        <div id="update-document-div">
          <UpdateDocumentContainer />
        </div>
        <div id="read-document-div">
          <ReadDocumentContainer />
        </div>
        <div id="delete-document-div">
          <DeleteDocumentContainer />
        </div>
        <div id="picture-div" className="col-2">
          <PictureContainer />
        </div>
        <div id="notifications-div" className="col-3">
          <NotificationsContainer />
        </div>
      </div>
    </SharedIdProvider>
  );
};

const root = createRoot(document.querySelector(".container"));
root.render(<App />);
