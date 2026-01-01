import {
  type onDisconnectPayload,
  type onLoadDocumentPayload,
  type onStoreDocumentPayload,
  Server
} from '@hocuspocus/server';
import os from 'os';
import { getOpenAPIDefinition } from "./page-service.ts";
import axios from "axios";
import { prosemirrorJSONToYDoc } from "y-prosemirror";
import { marks, schema as baseSchema } from "prosemirror-schema-basic";
import { persistDocument } from "./page-persistence.ts";
import { Schema } from "prosemirror-model";
import { addListNodes } from "prosemirror-schema-list";
import { PageVersion } from "./page-version.ts";
import { Doc } from "yjs";

axios.defaults.headers["X-Page-Authorization"] = "INTERNAL";

const PORT = Number(process.env.PORT || 8080);

const loadExtensions = async () => {
  const [
    { getRedisExtension },
    { getLoggerExtension },
  ] = await Promise.all([
    //@ts-ignore
    import('./extensions/redis.ts'),
    //@ts-ignore
    import('./extensions/logger.ts'),
  ]);

  return [
    getRedisExtension(),
    getLoggerExtension(),
  ];
};

const extensions = await loadExtensions();

const server = new Server({
  name: `collaboration-service-${os.hostname()}`,
  port: PORT,
  extensions,
  timeout: 30000,
  debounce: 5000,
  maxDebounce: 30000,
  quiet: true,
  onStoreDocument: async (data: onStoreDocumentPayload): Promise<any> => persistDocument(data.documentName, data.document),
  onLoadDocument: async (data: onLoadDocumentPayload): Promise<any> => {
    const documentId = data.documentName;
    const { data: snapshot } = await getOpenAPIDefinition().getSnapshot(documentId);

    if (snapshot) {
      const { contentJson } = snapshot;
      if (!contentJson) {
        PageVersion.setVersion(documentId, snapshot.version!)
        return new Doc();
      }
      console.log(contentJson)
      const schema = new Schema({
        nodes: addListNodes(baseSchema.spec.nodes, "paragraph block*", "block"),
        marks: {
          ...marks,
          bold: {}
        },
      });
      PageVersion.setVersion(documentId, snapshot.version!);
      return prosemirrorJSONToYDoc(schema, JSON.parse(contentJson));
    }

    PageVersion.setVersion(documentId, 0);
    return new Doc();
  },
  onDisconnect: async (data: onDisconnectPayload): Promise<any> => {
    const { clientsCount } = data;

    if (clientsCount === 0) {
      await persistDocument(data.documentName, data.document);
    }
    PageVersion.clearVersion(data.documentName);
    return Promise.resolve();
  }
});

server.listen().then(() => {
  console.log(`🚀 Collaboration service (Hocuspocus) running on port ${PORT}`);
});
