import { type onStoreDocumentPayload, Server } from '@hocuspocus/server';
import os from 'os';
import type { PageSnapshotV1 } from "./page-snapshot-v1.ts";
import {
  buildNormalizedSnapshot,
  validateSnapshot
} from "./pm-conversion-utils.ts";
import { getNextVersion } from "./page-version.ts";
import {
  type BlockSnapshotRequest,
  getOpenAPIDefinition
} from "./page-service.ts";
import axios from "axios";

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
  onStoreDocument: async (data: onStoreDocumentPayload): Promise<any> => {
    const pageId = data.documentName;
    const ydoc = data.document;

    const content = buildNormalizedSnapshot(ydoc);

    const snapshot: PageSnapshotV1 = {
      schemaVersion: 1,
      pageId,
      generatedAt: new Date().toISOString(),
      updatedBy: data.context.userId,
      source: "collaboration-service",
      content: content,
      version: getNextVersion(pageId)
    }

    const isSnapshotValid = validateSnapshot(snapshot);

    if (!isSnapshotValid) {
      console.error(`Invalid snapshot for page ${pageId}. Aborting save.`);
      return Promise.resolve();
    }

    getOpenAPIDefinition().saveSnapshot(pageId, snapshot as unknown as BlockSnapshotRequest).then(() => {
      console.log(`Snapshot for page ${pageId} saved successfully.`);
    }).catch(() => {
      console.error(`Failed to save snapshot for page ${pageId}.`);
    });


    console.log('SNAPSHOT (Hocuspocus debounced)', JSON.stringify(snapshot, null, 2));

    return Promise.resolve();
  },
});

server.listen().then(() => {
  console.log(`🚀 Collaboration service (Hocuspocus) running on port ${PORT}`);
});