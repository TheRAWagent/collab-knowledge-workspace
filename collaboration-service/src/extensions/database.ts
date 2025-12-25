import { Database } from "@hocuspocus/extension-database"
import {randomUUIDv7} from 'bun';

import {
  yXmlFragmentToProsemirrorJSON,
} from "y-prosemirror";

export const getDatabaseExtension = () => {
  return new Database({
    store: async ({document}) => {
      const xml = document.getXmlFragment("prosemirror")
      const json = yXmlFragmentToProsemirrorJSON(xml);
}
  });
}

