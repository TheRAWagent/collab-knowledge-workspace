import {type onStoreDocumentPayload, Server} from '@hocuspocus/server';
import os from 'os';

const PORT = Number(process.env.PORT || 8080);

const loadExtensions = async () => {
  const [
    { getRedisExtension },
    { getLoggerExtension },
    { getDatabaseExtension },
  ] = await Promise.all([
    //@ts-ignore
    import('./extensions/redis.ts'),
    //@ts-ignore
    import('./extensions/logger.ts'),
    //@ts-ignore
    import('./extensions/database.ts'),
  ]);

  return [
    getRedisExtension(),
    getLoggerExtension(),
    getDatabaseExtension(),
  ];
};

const extensions = await loadExtensions();

const server = new Server({
  name: `collaboration-service-${os.hostname()}`,
  port: PORT,
  extensions,
  async onConnect({request, context}) {},
  timeout: 30000,
  debounce: 5000,
  maxDebounce: 30000,
  quiet: true,
});

server.listen().then(() => {
  console.log(`🚀 Collaboration service (Hocuspocus) running on port ${PORT}`);
});