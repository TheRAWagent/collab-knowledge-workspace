import { Redis } from '@hocuspocus/extension-redis';

const REDIS_PORT = Number(process.env.REDIS_PORT) || 6379;
const REDIS_HOST = process.env.REDIS_HOST || 'localhost';

export const getRedisExtension = () => {
  return new Redis({
    host: REDIS_HOST,
    port: REDIS_PORT,
  });
};
